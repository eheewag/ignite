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

package org.apache.ignite.ml.regressions.logistic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.ignite.ml.TestUtils;
import org.apache.ignite.ml.common.TrainerTest;
import org.apache.ignite.ml.math.primitives.vector.Vector;
import org.apache.ignite.ml.math.primitives.vector.VectorUtils;
import org.apache.ignite.ml.nn.UpdatesStrategy;
import org.apache.ignite.ml.optimization.updatecalculators.SimpleGDParameterUpdate;
import org.apache.ignite.ml.optimization.updatecalculators.SimpleGDUpdateCalculator;
import org.junit.Test;

/**
 * Tests for {@link LogisticRegressionSGDTrainer}.
 */
public class LogisticRegressionSGDTrainerTest extends TrainerTest {
    /**
     * Test trainer on classification model y = x.
     */
    @Test
    public void trainWithTheLinearlySeparableCase() {
        Map<Integer, double[]> cacheMock = new HashMap<>();

        for (int i = 0; i < twoLinearlySeparableClasses.length; i++)
            cacheMock.put(i, twoLinearlySeparableClasses[i]);

        LogisticRegressionSGDTrainer trainer = new LogisticRegressionSGDTrainer()
            .withUpdatesStgy(new UpdatesStrategy<>(new SimpleGDUpdateCalculator(0.2),
                SimpleGDParameterUpdate::sumLocal, SimpleGDParameterUpdate::avg))
            .withMaxIterations(100000)
            .withLocIterations(100)
            .withBatchSize(14)
            .withSeed(123L);

        LogisticRegressionModel mdl = trainer.fit(
            cacheMock,
            parts,
            (k, v) -> VectorUtils.of(Arrays.copyOfRange(v, 1, v.length)),
            (k, v) -> v[0]
        );

        TestUtils.assertEquals(0, mdl.apply(VectorUtils.of(100, 10)), PRECISION);
        TestUtils.assertEquals(1, mdl.apply(VectorUtils.of(10, 100)), PRECISION);
    }

    /** */
    @Test
    public void testUpdate() {
        Map<Integer, double[]> cacheMock = new HashMap<>();

        for (int i = 0; i < twoLinearlySeparableClasses.length; i++)
            cacheMock.put(i, twoLinearlySeparableClasses[i]);

        LogisticRegressionSGDTrainer trainer = new LogisticRegressionSGDTrainer()
            .withUpdatesStgy(new UpdatesStrategy<>(new SimpleGDUpdateCalculator(0.2),
                SimpleGDParameterUpdate::sumLocal, SimpleGDParameterUpdate::avg))
            .withMaxIterations(100000)
            .withLocIterations(100)
            .withBatchSize(10)
            .withSeed(123L);

        LogisticRegressionModel originalMdl = trainer.fit(
            cacheMock,
            parts,
            (k, v) -> VectorUtils.of(Arrays.copyOfRange(v, 1, v.length)),
            (k, v) -> v[0]
        );

        LogisticRegressionModel updatedOnSameDS = trainer.update(
            originalMdl,
            cacheMock,
            parts,
            (k, v) -> VectorUtils.of(Arrays.copyOfRange(v, 1, v.length)),
            (k, v) -> v[0]
        );

        LogisticRegressionModel updatedOnEmptyDS = trainer.update(
            originalMdl,
            new HashMap<Integer, double[]>(),
            parts,
            (k, v) -> VectorUtils.of(Arrays.copyOfRange(v, 1, v.length)),
            (k, v) -> v[0]
        );

        Vector v1 = VectorUtils.of(100, 10);
        Vector v2 = VectorUtils.of(10, 100);
        TestUtils.assertEquals(originalMdl.apply(v1), updatedOnSameDS.apply(v1), PRECISION);
        TestUtils.assertEquals(originalMdl.apply(v2), updatedOnSameDS.apply(v2), PRECISION);
        TestUtils.assertEquals(originalMdl.apply(v2), updatedOnEmptyDS.apply(v2), PRECISION);
        TestUtils.assertEquals(originalMdl.apply(v1), updatedOnEmptyDS.apply(v1), PRECISION);
    }
}
