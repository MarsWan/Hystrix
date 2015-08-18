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

public class RollingNumberPerfTest {

	public static class CounterState {
		HystrixRollingNumber counter;

		public void setUp() {
			counter = new HystrixRollingNumber(100, 10);
		}
	}

	public static class ValueState {
		final Random r = new Random();

		int value;
		HystrixRollingNumberEvent type;

		public void setUp() {
			value = r.nextInt(100);
			int typeInt = r.nextInt(3);
			switch (typeInt) {
			case 0:
				type = HystrixRollingNumberEvent.SUCCESS;
				break;
			case 1:
				type = HystrixRollingNumberEvent.FAILURE;
				break;
			case 2:
				type = HystrixRollingNumberEvent.TIMEOUT;
				break;
			default:
				throw new RuntimeException("Unexpected : " + typeInt);
			}
		}
	}

	public HystrixRollingNumber writeOnly(CounterState counterState,
			ValueState valueState) {
		counterState.counter.add(valueState.type, valueState.value);
		return counterState.counter;
	}

	public long readOnly(CounterState counterState) {
		HystrixRollingNumber counter = counterState.counter;
		return counter.getCumulativeSum(HystrixRollingNumberEvent.SUCCESS)
				+ counter.getCumulativeSum(HystrixRollingNumberEvent.FAILURE)
				+ counter.getCumulativeSum(HystrixRollingNumberEvent.TIMEOUT)
				+ counter.getRollingSum(HystrixRollingNumberEvent.SUCCESS)
				+ counter.getRollingSum(HystrixRollingNumberEvent.FAILURE)
				+ counter.getRollingSum(HystrixRollingNumberEvent.TIMEOUT);
	}

	public HystrixRollingNumber writeHeavyCounterAdd(CounterState counterState,
			ValueState valueState) {
		counterState.counter.add(valueState.type, valueState.value);
		return counterState.counter;
	}

	public long writeHeavyReadMetrics(CounterState counterState) {
		HystrixRollingNumber counter = counterState.counter;
		return counter.getCumulativeSum(HystrixRollingNumberEvent.SUCCESS)
				+ counter.getCumulativeSum(HystrixRollingNumberEvent.FAILURE)
				+ counter.getCumulativeSum(HystrixRollingNumberEvent.TIMEOUT)
				+ counter.getRollingSum(HystrixRollingNumberEvent.SUCCESS)
				+ counter.getRollingSum(HystrixRollingNumberEvent.FAILURE)
				+ counter.getRollingSum(HystrixRollingNumberEvent.TIMEOUT);
	}

	public HystrixRollingNumber evenSplitCounterAdd(CounterState counterState,
			ValueState valueState) {
		counterState.counter.add(valueState.type, valueState.value);
		return counterState.counter;
	}

	public long evenSplitReadMetrics(CounterState counterState) {
		HystrixRollingNumber counter = counterState.counter;
		return counter.getCumulativeSum(HystrixRollingNumberEvent.SUCCESS)
				+ counter.getCumulativeSum(HystrixRollingNumberEvent.FAILURE)
				+ counter.getCumulativeSum(HystrixRollingNumberEvent.TIMEOUT)
				+ counter.getRollingSum(HystrixRollingNumberEvent.SUCCESS)
				+ counter.getRollingSum(HystrixRollingNumberEvent.FAILURE)
				+ counter.getRollingSum(HystrixRollingNumberEvent.TIMEOUT);
	}

	public HystrixRollingNumber readHeavyCounterAdd(CounterState counterState,
			ValueState valueState) {
		counterState.counter.add(valueState.type, valueState.value);
		return counterState.counter;
	}

	public long readHeavyReadMetrics(CounterState counterState) {
		HystrixRollingNumber counter = counterState.counter;
		return counter.getCumulativeSum(HystrixRollingNumberEvent.SUCCESS)
				+ counter.getCumulativeSum(HystrixRollingNumberEvent.FAILURE)
				+ counter.getCumulativeSum(HystrixRollingNumberEvent.TIMEOUT)
				+ counter.getRollingSum(HystrixRollingNumberEvent.SUCCESS)
				+ counter.getRollingSum(HystrixRollingNumberEvent.FAILURE)
				+ counter.getRollingSum(HystrixRollingNumberEvent.TIMEOUT);
	}
}
