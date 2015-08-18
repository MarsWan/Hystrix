/**
 * Copyright 2015 Netflix, Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.hystrix.perf;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixThreadPool;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

public class CommandGroupKeyExecutionPerfTest {

	public static class CommandState {
		HystrixCommand<Integer> command;

		static class TestCommand extends HystrixCommand<Integer> {
			TestCommand() {
				super(HystrixCommandGroupKey.Factory.asKey("PERF"));
			}

			@Override
			protected Integer run() throws Exception {
				return 1;
			}

			@Override
			protected Integer getFallback() {
				return 2;
			}
		}

		public void setUp() {
			command = new TestCommand();
		}
	}

	public static class ExecutorState {
		ExecutorService executorService;

		public void setUp() {
			executorService = Executors.newFixedThreadPool(100);
		}

		public void tearDown() {
			List<Runnable> runnables = executorService.shutdownNow();
		}
	}

	public static class ThreadPoolState {
		HystrixThreadPool hystrixThreadPool;

		public void setUp() {
			hystrixThreadPool = new HystrixThreadPool.HystrixThreadPoolDefault(
					HystrixThreadPoolKey.Factory.asKey("PERF"),
					HystrixThreadPoolProperties.Setter().withCoreSize(100));
		}

		public void tearDown() {
			hystrixThreadPool.getExecutor().shutdownNow();
		}
	}

	public Integer baselineExecute() {
		return 1;
	}

	public Integer baselineQueue(ExecutorState state)
			throws InterruptedException, ExecutionException {
		try {
			return state.executorService.submit(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					return 1;
				}
			}).get();
		} catch (Throwable t) {
			return 2;
		}
	}

	public Integer baselineSyncObserve() throws InterruptedException {
		Observable<Integer> syncObservable = Observable.just(1);

		try {
			return syncObservable.toBlocking().first();
		} catch (Throwable t) {
			return 2;
		}
	}

	public Integer baselineAsyncComputationObserve()
			throws InterruptedException {
		Observable<Integer> asyncObservable = Observable.just(1).subscribeOn(
				Schedulers.computation());

		try {
			return asyncObservable.toBlocking().first();
		} catch (Throwable t) {
			return 2;
		}
	}

	public Integer baselineAsyncCustomThreadPoolObserve(ThreadPoolState state) {
		Observable<Integer> asyncObservable = Observable.just(1).subscribeOn(
				state.hystrixThreadPool.getScheduler());
		try {
			return asyncObservable.toBlocking().first();
		} catch (Throwable t) {
			return 2;
		}
	}

	public Integer hystrixExecute(CommandState state) {
		return state.command.execute();
	}

	public Integer hystrixQueue(CommandState state) {
		try {
			return state.command.queue().get();
		} catch (Throwable t) {
			return 2;
		}
	}

	public Integer hystrixObserve(CommandState state) {
		return state.command.observe().toBlocking().first();
	}

	public Integer hystrixToObservable(CommandState state) {
		return state.command.toObservable().toBlocking().first();
	}
}
