package com.leehc.course.rabbitmqchat.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.leehc.course.rabbitmqchat.service.Receiver;
import com.leehc.course.rabbitmqchat.service.Sender;

@Profile({"RabbitChat"})
@Configuration
public class RabbitmqChatConfig {


    @Profile("receiver")
    static class ReceiverConfig {

        @Bean
        Declarables queues() {
            return new Declarables(
                new Queue("commandQueue"),
                new Queue("userQueue"),
                new Queue("roomQueue")
            );
        }

        @Bean
        Declarables topicExchanges() {
            return new Declarables(
                new TopicExchange("request"),
                new TopicExchange("chat")
            );
        }

        @Bean
        Declarables fanoutExchanges() {
            return new Declarables(
                new FanoutExchange("userExchange"),
                new FanoutExchange("roomExchange")
            );
        }

        @Bean
        Binding bindingRequestToCommand(TopicExchange request, Queue commandQueue) {
            return BindingBuilder.bind(commandQueue)
                .to(request)
                .with("command.#");
        }

        @Bean
        Binding bindingRequestToChat(TopicExchange request, TopicExchange chat) {
            return BindingBuilder.bind(chat)
                .to(request)
                .with("chat.#");
        }

        @Bean
        Binding bindingChatToUser(TopicExchange chat, FanoutExchange user) {
            return BindingBuilder.bind(user)
                .to(chat)
                .with("*.user.#");
        }

        @Bean
        Binding bindingChatToRoom(TopicExchange chat, FanoutExchange room) {
            return BindingBuilder.bind(room)
                .to(chat)
                .with("*.room.#");
        }

        @Bean
        Binding bindingUserToUser(FanoutExchange userExchange, Queue userQueue) {
            return BindingBuilder.bind(userQueue)
                .to(userExchange);
        }

        @Bean
        Binding bindingRoomToRoom(FanoutExchange roomExchange, Queue roomQueue) {
            return BindingBuilder.bind(roomQueue)
                .to(roomExchange);
        }

        @Bean    
		Receiver receiver() {
	 	 	return new Receiver();
		}
    }
	

	@Profile("sender")
	@Bean
	Sender sender() {
		return new Sender();
	}
}
