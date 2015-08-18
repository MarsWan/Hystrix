/**
 * Copyright 2015 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.hystrix.perf;

import java.util.Random;

import com.netflix.hystrix.strategy.properties.HystrixProperty;
import com.netflix.hystrix.util.HystrixRollingPercentile;

public class RollingPercentilePerfTest {

	public static class PercentileState {
		HystrixRollingPercentile percentile;

		public boolean percentileEnabled;

		public void setUp() {
			percentile = new HystrixRollingPercentile(100, 10, 1000,
					HystrixProperty.Factory.asProperty(percentileEnabled));
		}
	}

	public static class LatencyState {
		final Random r = new Random();

		int latency;

		public void setUp() {
			latency = r.nextInt(100);
		}
	}

	public HystrixRollingPercentile writeOnly(PercentileState percentileState,
			LatencyState latencyState) {
		percentileState.percentile.addValue(latencyState.latency);
		return percentileState.percentile;
	}

	public int readOnly(PercentileState percentileState) {
		HystrixRollingPercentile percentile = percentileState.percentile;
		return percentile.getMean() + percentile.getPercentile(10)
				+ percentile.getPercentile(25) + percentile.getPercentile(50)
				+ percentile.getPercentile(75) + percentile.getPercentile(90)
				+ percentile.getPercentile(95) + percentile.getPercentile(99)
				+ percentile.getPercentile(99.5);
	}

	public HystrixRollingPercentile writeHeavyLatencyAdd(
			PercentileState percentileState, LatencyState latencyState) {
		percentileState.percentile.addValue(latencyState.latency);
		return percentileState.percentile;
	}

	public int writeHeavyReadMetrics(PercentileState percentileState) {
		HystrixRollingPercentile percentile = percentileState.percentile;
		return percentile.getMean() + percentile.getPercentile(10)
				+ percentile.getPercentile(25) + percentile.getPercentile(50)
				+ percentile.getPercentile(75) + percentile.getPercentile(90)
				+ percentile.getPercentile(95) + percentile.getPercentile(99)
				+ percentile.getPercentile(99.5);
	}

	public HystrixRollingPercentile evenSplitLatencyAdd(
			PercentileState percentileState, LatencyState latencyState) {
		percentileState.percentile.addValue(latencyState.latency);
		return percentileState.percentile;
	}

	public int evenSplitReadMetrics(PercentileState percentileState) {
		HystrixRollingPercentile percentile = percentileState.percentile;
		return percentile.getMean() + percentile.getPercentile(10)
				+ percentile.getPercentile(25) + percentile.getPercentile(50)
				+ percentile.getPercentile(75) + percentile.getPercentile(90)
				+ percentile.getPercentile(95) + percentile.getPercentile(99)
				+ percentile.getPercentile(99.5);
	}

	public HystrixRollingPercentile readHeavyLatencyAdd(
			PercentileState percentileState, LatencyState latencyState) {
		percentileState.percentile.addValue(latencyState.latency);
		return percentileState.percentile;
	}

	public int readHeavyReadMetrics(PercentileState percentileState) {
		HystrixRollingPercentile percentile = percentileState.percentile;
		return percentile.getMean() + percentile.getPercentile(10)
				+ percentile.getPercentile(25) + percentile.getPercentile(50)
				+ percentile.getPercentile(75) + percentile.getPercentile(90)
				+ percentile.getPercentile(95) + percentile.getPercentile(99)
				+ percentile.getPercentile(99.5);
	}
}
