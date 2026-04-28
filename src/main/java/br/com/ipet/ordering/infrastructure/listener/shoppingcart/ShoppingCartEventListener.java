package br.com.ipet.ordering.infrastructure.listener.shoppingcart;

import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCartCreatedEvent;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCartDiscartedEvent;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCartEmpitiedEvent;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCartItemAddedEvent;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCartItemRemovedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShoppingCartEventListener {

    @EventListener
    public void onCreate(ShoppingCartCreatedEvent event) {
        log.info("created");
    }

    @EventListener
    public void onAddedItem(ShoppingCartItemAddedEvent event) {
        log.info("item added");
    }

    @EventListener
    public void onRemovedItem(ShoppingCartItemRemovedEvent event) {
        log.info("item removed");
    }

    @EventListener
    public void onEmpited(ShoppingCartEmpitiedEvent event) {
        log.info("empited");
    }

    @EventListener
    public void onDiscarted(ShoppingCartDiscartedEvent event) {
        log.info("discarted");
    }
}
