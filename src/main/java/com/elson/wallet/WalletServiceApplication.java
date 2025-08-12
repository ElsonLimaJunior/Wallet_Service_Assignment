package com.elson.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WalletServiceApplication {

	/**
	 * O método main, padrão em qualquer aplicação Java, que delega o controle
	 * para a classe SpringApplication para inicializar o contexto do Spring.
	 *
	 * @param args Argumentos de linha de comando (não utilizados neste projeto).
	 */
	public static void main(String[] args) {
		SpringApplication.run(WalletServiceApplication.class, args);
	}

}