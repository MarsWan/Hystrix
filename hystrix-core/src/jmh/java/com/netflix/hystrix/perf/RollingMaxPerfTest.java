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

import com.netflix.hystrix.util.HystrixRollingNumber;
import com.netflix.hystrix.util.HystrixRollingNumberEvent;

public class RollingMaxPerfTest {

	public static class CounterState {
		HystrixRollingNumber counter;

		public void setUp() {
			counter = new HystrixRollingNumber(100, 10);
		}
	}

	public static class ValueState {
		final Random r = new Random();

		int value;

		public void setUp() {
			value = r.nextInt(100);
		}
	}

	public HystrixRollingNumber writeOnly(CounterState counterState,
			ValueState valueState) {
		counterState.counter.updateRollingMax(
				HystrixRollingNumberEvent.COMMAND_MAX_ACTIVE, valueState.value);
		return counterState.counter;
	}

	public long readOnly(CounterState counterState) {
		HystrixRollingNumber counter = counterState.counter;
		return counter
				.getRollingMaxValue(HystrixRollingNumberEvent.COMMAND_MAX_ACTIVE);
	}

	public HystrixRollingNumber writeHeavyUpdateMax(CounterState counterState,
			ValueState valueState) {
		counterState.counter.updateRollingMax(
				HystrixRollingNumberEvent.COMMAND_MAX_ACTIVE, valueState.value);
		return counterState.counter;
	}

	public long writeHeavyReadMetrics(CounterState counterState) {
		HystrixRollingNumber counter = counterState.counter;
		return counter
				.getRollingMaxValue(HystrixRollingNumberEvent.COMMAND_MAX_ACTIVE);
	}

	public HystrixRollingNumber evenSplitUpdateMax(CounterState counterState,
			ValueState valueState) {
		counterState.counter.updateRollingMax(
				HystrixRollingNumberEvent.COMMAND_MAX_ACTIVE, valueState.value);
		return counterState.counter;
	}

	public long evenSplitReadMetrics(CounterState counterState) {
		HystrixRollingNumber counter = counterState.counter;
		return counter
				.getRollingMaxValue(HystrixRollingNumberEvent.COMMAND_MAX_ACTIVE);
	}

	public HystrixRollingNumber readHeavyUpdateMax(CounterState counterState,
			ValueState valueState) {
		counterState.counter.updateRollingMax(
				HystrixRollingNumberEvent.COMMAND_MAX_ACTIVE, valueState.value);
		return counterState.counter;
	}

	public long readHeavyReadMetrics(CounterState counterState) {
		HystrixRollingNumber counter = counterState.counter;
		return counter
				.getRollingMaxValue(HystrixRollingNumberEvent.COMMAND_MAX_ACTIVE);
	}
}
