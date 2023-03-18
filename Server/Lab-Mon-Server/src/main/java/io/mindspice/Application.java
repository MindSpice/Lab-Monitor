package io.mindspice;

import io.mindspice.monitor.ClientMonitor;
import io.mindspice.monitor.NutMonitor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Application {
	private static final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		try {
			exec.scheduleAtFixedRate(new ClientMonitor(), 0,
					Settings.get().clientPollRate, TimeUnit.SECONDS);
		} catch (IOException e) {
			System.out.println("Failed To Start Info Monitor/Server");
			throw new RuntimeException(e);
		}

		if (Settings.get().isNUTMonitor) {
			try {
				exec.scheduleAtFixedRate(new NutMonitor(), Settings.get().nutPollRate,
						Settings.get().nutPollRate, TimeUnit.SECONDS);
			} catch (Exception e) {
				System.out.println("Failed To Start Nut Monitor");
				throw new RuntimeException(e);
			}
		}
	}

}
