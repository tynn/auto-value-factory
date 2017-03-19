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

import com.google.auto.common.AnnotationMirrors;
import com.google.auto.value.extension.AutoValueExtension;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.TypeElement;

import tynn.auto.value.factory.AutoValueFactory;

public class AutoValueFactoryExtension extends AutoValueExtension {

    @Override
    public boolean applicable(Context context) {
        return context.autoValueClass().getAnnotation(AutoValueFactory.class) != null;
    }

    @Override
    public boolean mustBeFinal(Context context) {
        return true;
    }

    @Override
    public String generateClass(Context context, String className, String classToExtend, boolean isFinal) {
        try {
            return AutoValueFactoryFiler
                    .in(context.packageName())
                    .generatedBy(getClass())
                    .classDefinition(className, classToExtend)
                    .autoFactoryDefinition(autoValueFactory(context.autoValueClass()))
                    .constructorDefinition(className)
                    .constructorParameters(context.properties())
                    .constructorImplementation(context.properties().keySet())
                    .closeConstructor()
                    .closeClass()
                    .toString();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static AnnotationValue autoValueFactory(TypeElement autoValueClass) {
        String autoValueFactory = AutoValueFactory.class.getCanonicalName();
        for (AnnotationMirror annotation : autoValueClass.getAnnotationMirrors()) {
            if (annotation.getAnnotationType().toString().equals(autoValueFactory)) {
                return AnnotationMirrors.getAnnotationValue(annotation, "value");
            }
        }
        throw new IllegalStateException("AutoValueFactory not applicable");
    }
}
