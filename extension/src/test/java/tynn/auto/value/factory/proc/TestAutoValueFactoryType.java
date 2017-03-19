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

import com.google.auto.factory.AutoFactory;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

import tynn.auto.value.factory.AutoValueFactory;
import tynn.auto.value.factory.FactoryProvided;

@AutoValue
@AutoValueFactory(@AutoFactory(className = "TestFactory"))
public abstract class TestAutoValueFactoryType {

    @FactoryProvided
    @TestQualifier
    @TestAnnotation
    public abstract String provided();

    @FactoryProvided
    @Nullable
    @TestAnnotation
    public abstract String nullable();

    @Deprecated
    @TestQualifier
    @TestAnnotation
    public abstract String notProvided();
}
