package eclipsemag.fx.styledtext;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.fx.core.Subscription;
import org.eclipse.fx.core.ThreadSynchronize;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class FXThreadSync implements ThreadSynchronize {

		@Override
		public <V> Future<V> asyncExec(Callable<V> callable) {
			RunnableFuture<V> task = new FutureTask<V>(callable);
			javafx.application.Platform.runLater(task);
			return task;
		}

		@Override
		public void asyncExec(Runnable runnable) {
			javafx.application.Platform.runLater(runnable);
		}

		@Override
		public Subscription scheduleExecution(long delay, Runnable runnable) {
			final AtomicBoolean b = new AtomicBoolean(true);
			Timeline t = new Timeline(new KeyFrame(Duration.millis(delay), (a) -> {
				if (b.get()) {
					runnable.run();
				}
			}));
			t.play();
			return new Subscription() {

				@Override
				public void dispose() {
					b.set(false);
					t.stop();
				}
			};
		}

		@Override
		public <T> CompletableFuture<T> scheduleExecution(long delay, Callable<T> runnable) {
			CompletableFuture<T> future = new CompletableFuture<T>();

			Timeline t = new Timeline(new KeyFrame(Duration.millis(delay), (a) -> {
				try {
					if (!future.isCancelled()) {
						future.complete(runnable.call());
					}
				} catch (Exception e) {
					future.completeExceptionally(e);
				}
			}));
			t.play();

			return future;
		}

		@Override
		public void syncExec(Runnable runnable) {
			if (javafx.application.Platform.isFxApplicationThread()) {
				runnable.run();
			} else {
				RunnableFuture<?> task = new FutureTask<Void>(runnable, null);
				javafx.application.Platform.runLater(task);
				try {
					task.get(); // wait for task to complete
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				} finally {
					task.cancel(true);
				}
			}
		}

		@Override
		public <V> V syncExec(Callable<V> callable, V defaultValue) {
			if (javafx.application.Platform.isFxApplicationThread()) {
				try {
					return callable.call();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else {
				RunnableFuture<V> task = new FutureTask<V>(callable);
				javafx.application.Platform.runLater(task);
				try {
					return task.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				} finally {
					task.cancel(true);
				}
			}
			return defaultValue;
		}

	}