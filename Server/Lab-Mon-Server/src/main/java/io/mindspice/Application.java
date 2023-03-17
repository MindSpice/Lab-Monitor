package io.mindspice;

import io.mindspice.monitor.InfoMonitor;
import io.mindspice.monitor.NutMonitor;
import io.mindspice.networking.InfoServer;
import io.mindspice.state.NutState;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
			exec.scheduleAtFixedRate(new InfoMonitor(), 0,
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
