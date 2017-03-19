/*
 * Copyright (C) 2017 Christian Schmitz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tynn.auto.value.factory.proc;

import com.google.auto.factory.Provided;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Generated;
import javax.inject.Qualifier;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import tynn.auto.value.factory.FactoryProvided;

class AutoValueFactoryFiler {

    private static final Locale L = Locale.US;

    private final StringBuilder sb;

    private AutoValueFactoryFiler() {
        sb = new StringBuilder();
    }

    private AutoValueFactoryFiler writeLine(String format, Object... args) {
        String line = String.format(L, format, args);
        sb.append(line).append('\n');
        return this;
    }

    private AutoValueFactoryFiler removeSeparatorLineEnd() {
        sb.setLength(sb.length() - 2);
        return writeLine("");
    }

    static AutoValueFactoryFiler in(String packageName) {
        return new AutoValueFactoryFiler()
                .writeLine("package %s;", packageName);
    }

    AutoValueFactoryFiler generatedBy(Class<?> by) {
        return writeLine("@%s(\"%s\")", Generated.class.getCanonicalName(), by.getCanonicalName());
    }

    AutoValueFactoryFiler autoFactoryDefinition(AnnotationValue value)
            throws InvocationTargetException, IllegalAccessException {
        return writeLine("  %s", value.getValue());
    }

    AutoValueFactoryFiler classDefinition(String className, String classToExtend) {
        return writeLine("final class %1$s extends %2$s {", className, classToExtend);
    }

    AutoValueFactoryFiler constructorDefinition(String className) {
        return writeLine("  %s(", className);
    }

    AutoValueFactoryFiler constructorParameters(Map<String, ExecutableElement> properties) {
        for (Map.Entry<String, ExecutableElement> property : properties.entrySet()) {
            ExecutableElement element = property.getValue();
            if (element.getAnnotation(FactoryProvided.class) != null) {
                writeLine("    @%s", Provided.class.getCanonicalName());
            }
            for (AnnotationMirror annotation : element.getAnnotationMirrors()) {
                if (nullableOrQualifier(annotation.getAnnotationType().asElement())) {
                    writeLine("    %s", annotation);
                }
            }
            writeLine("    %s %s,", element.getReturnType(), property.getKey());
        }
        return removeSeparatorLineEnd();
    }

    private boolean nullableOrQualifier(Element annotation) {
        if (annotation.getSimpleName().contentEquals("Nullable")) {
            return true;
        }
        return annotation.getAnnotation(Qualifier.class) != null;
    }

    AutoValueFactoryFiler constructorImplementation(Set<String> properties) {
        writeLine("  ) {").writeLine("    super(");
        for (String property : properties) {
            writeLine("      %s,", property);
        }
        return removeSeparatorLineEnd();
    }

    AutoValueFactoryFiler closeConstructor() {
        return writeLine("    );").writeLine("  }");
    }

    AutoValueFactoryFiler closeClass() {
        return writeLine("}");
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
