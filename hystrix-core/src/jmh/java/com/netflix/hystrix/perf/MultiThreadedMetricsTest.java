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

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandMetrics;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;

public class MultiThreadedMetricsTest {

	public static class CommandState {
		HystrixCommand<Integer> command;

		public HystrixCommandProperties.ExecutionIsolationStrategy isolationStrategy;

		public void setUp() {
			command = new HystrixCommand<Integer>(HystrixCommand.Setter
					.withGroupKey(HystrixCommandGroupKey.Factory.asKey("PERF"))
					.andCommandPropertiesDefaults(
							HystrixCommandProperties
									.Setter()
									.withExecutionIsolationStrategy(
											isolationStrategy)
									.withRequestCacheEnabled(true)
									.withRequestLogEnabled(true)
									.withCircuitBreakerEnabled(true)
									.withCircuitBreakerForceOpen(false))
					.andThreadPoolPropertiesDefaults(
							HystrixThreadPoolProperties.Setter().withCoreSize(
									100))) {
				@Override
				protected Integer run() throws Exception {
					return 1;
				}

				@Override
				protected Integer getFallback() {
					return 2;
				}
			};
		}
	}

	public Integer writeHeavyCommandExecution(CommandState state) {
		return state.command.observe().toBlocking().first();
	}

	public Integer writeHeavyReadMetrics(CommandState state) {
		HystrixCommandMetrics metrics = state.command.getMetrics();
		return metrics.getExecutionTimeMean()
				+ metrics.getExecutionTimePercentile(50)
				+ metrics.getExecutionTimePercentile(75)
				+ metrics.getExecutionTimePercentile(99);
	}

	public Integer evenSplitOfWritesAndReadsCommandExecution(CommandState state) {
		return state.command.observe().toBlocking().first();
	}

	public Integer evenSplitOfWritesAndReadsReadMetrics(CommandState state) {
		HystrixCommandMetrics metrics = state.command.getMetrics();
		return metrics.getExecutionTimeMean()
				+ metrics.getExecutionTimePercentile(50)
				+ metrics.getExecutionTimePercentile(75)
				+ metrics.getExecutionTimePercentile(99);
	}

	public Integer readHeavyCommandExecution(CommandState state) {
		return state.command.observe().toBlocking().first();
	}

	public Integer readHeavyReadMetrics(CommandState state) {
		HystrixCommandMetrics metrics = state.command.getMetrics();
		return metrics.getExecutionTimeMean()
				+ metrics.getExecutionTimePercentile(50)
				+ metrics.getExecutionTimePercentile(75)
				+ metrics.getExecutionTimePercentile(99);
	}
}
