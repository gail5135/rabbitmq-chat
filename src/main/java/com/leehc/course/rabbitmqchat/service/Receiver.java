package com.leehc.course.rabbitmqchat.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;

public class Receiver {

    @RabbitListener(queues = "commandQueue")
	public void receive1(String in) throws InterruptedException {
		receive(in, 1);
	}
    
    @RabbitListener(queues = "userQueue")
	public void receive2(String in) throws InterruptedException {
		receive(in, 2);
	}

    @RabbitListener(queues = "roomQueue")
	public void receive3(String in) throws InterruptedException {
		receive(in, 3);
	}

    public void receive(String in, int receiver) throws
	    InterruptedException {
		StopWatch watch = new StopWatch();
		watch.start();
		System.out.println("instance " + receiver + " [x] Received '"
		    + in + "'");
		doWork(in);
		watch.stop();
		System.out.println("instance " + receiver + " [x] Done in "
		    + watch.getTotalTimeSeconds() + "s");
	}

	private void doWork(String in) throws InterruptedException {
		for (char ch : in.toCharArray()) {
			if (ch == '.') {
				Thread.sleep(1000);
			}
		}
	}
}
