/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.serialization.runners;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlinx.serialization.TestGeneratorKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("plugins/kotlinx-serialization/testData/compilerFacility")
@TestDataPath("$PROJECT_ROOT")
public class CompilerFacilityTestForSerializationGenerated extends AbstractCompilerFacilityTestForSerialization {
  @Test
  public void testAllFilesPresentInCompilerFacility() {
    KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/kotlinx-serialization/testData/compilerFacility"), Pattern.compile("^(.+)\\.kt$"), null, true);
  }

  @Test
  @TestMetadata("serializationPlugin.kt")
  public void testSerializationPlugin() {
    runTest("plugins/kotlinx-serialization/testData/compilerFacility/serializationPlugin.kt");
  }
}
