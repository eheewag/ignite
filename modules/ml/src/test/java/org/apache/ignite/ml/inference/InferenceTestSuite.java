/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.ml.inference;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestSuite;
import org.apache.ignite.ml.inference.builder.IgniteDistributedInfModelBuilderTest;
import org.apache.ignite.ml.inference.builder.SingleInfModelBuilderTest;
import org.apache.ignite.ml.inference.builder.ThreadedInfModelBuilderTest;
import org.apache.ignite.ml.inference.storage.model.DefaultModelStorageTest;
import org.apache.ignite.ml.inference.util.DirectorySerializerTest;
import org.junit.runner.RunWith;
import org.junit.runners.AllTests;

/**
 * Test suite for all tests located in {@link org.apache.ignite.ml.inference} package.
 */
@RunWith(AllTests.class)
public class InferenceTestSuite {
    /** */
    public static TestSuite suite() {
        TestSuite suite = new TestSuite();

        /** JUnit 4 tests. */
        suite.addTest(new JUnit4TestAdapter(SingleInfModelBuilderTest.class));
        suite.addTest(new JUnit4TestAdapter(ThreadedInfModelBuilderTest.class));
        suite.addTest(new JUnit4TestAdapter(DirectorySerializerTest.class));
        suite.addTest(new JUnit4TestAdapter(DefaultModelStorageTest.class));

        /** JUnit 3 tests. */
        suite.addTestSuite(IgniteDistributedInfModelBuilderTest.class);

        return suite;
    }
}
