package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import entities.Banco;
import entities.Conta;
import entities.EtapaEmprestimo;
import entities.EtapaLImite;
import entities.credito;
import entities.debito;
import model.exceptions.BusinessException;

public class Main {

	public static void main(String[] args) {

		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);

		List<Banco> contas = new ArrayList<>();

		System.out.println("====== BANCO CENTRAL JAVA ======");

		int menu = 0;
		while (menu != 8) {

			try {
				System.out.println("\n===== Menu ======");
				System.out.println(
						"1 - Cadastrar conta \n2 - Verificar conta do usuário \n3 - Contas registradas \n4 - Saque \n5 - Deposito \n6 - Empréstimos \n7 - Limite de Crédito \n8 - Sair");
				System.out.print("Escolha uma opção: ");
				menu = sc.nextInt();
				sc.nextLine();

				double buscarNumeroConta;
				Banco contaEncontrada;

				switch (menu) {
				case 1:
					System.out.println("====== Cadastrar conta ========");
					System.out.print("Qual tipo do cartão: (1 - Crédito / 2 - Débito): ");
					int tipo = sc.nextInt();
					sc.nextLine();
					System.out.print("Nome do cliente: ");
					String nome = sc.nextLine();
					System.out.print("Numero da conta: ");
					double conta = sc.nextDouble();
					System.out.print("Score da conta: ");
					int score = sc.nextInt();
					sc.nextLine();

					Conta cadastros = null;
					if (tipo == 1) {
						System.out.print("Insira o limite do cartão de credito: ");
						double limiteDisponivel = sc.nextDouble();
						sc.nextLine();

						EtapaLImite statusCredito = EtapaLImite.PENDENTE;
						cadastros = new credito(conta, nome, score, limiteDisponivel);
						Banco contasList = new Banco(cadastros, statusCredito);
						contas.add(contasList);

					} else {
						System.out.print("Insira o saldo da conta: ");
						double saldo = sc.nextDouble();
						sc.nextLine();

						EtapaEmprestimo statusDedito = EtapaEmprestimo.PENDENTE;
						cadastros = new debito(conta, nome, score, saldo);
						Banco contasList = new Banco(cadastros, statusDedito);
						contas.add(contasList);
					}
					break;

				case 2:
					System.out.println("====== Verificar conta do usuário ========");
					System.out.print("Insira o numero da conta que deseja buscar: ");
					buscarNumeroConta = sc.nextDouble();
					sc.nextLine();

					contaEncontrada = buscarConta(contas, buscarNumeroConta);

					if (contaEncontrada != null) {
						System.out.println("Conta localizada: " + contaEncontrada);
					} else {
						System.out.println("Conta não encontrada no sistema!");
					}
					break;

				case 3:
					System.out.println("====== Contas registradas ========");
					for (Banco c : contas) {
						System.out.println(c);
					}
					break;

				case 4:
					System.out.println("====== Saque ========");
					System.out.print("Insira o numero da conta para saque: ");
					buscarNumeroConta = sc.nextDouble();
					sc.nextLine();

					contaEncontrada = buscarConta(contas, buscarNumeroConta);

					if (contaEncontrada != null) {
						System.out.print("Quanto deseja sacar: ");
						double saque = sc.nextDouble();
						sc.nextLine();

						Conta contaGenerica = contaEncontrada.getConta();

						if (contaGenerica instanceof debito) {
							debito contaDebito = (debito) contaGenerica;
							contaDebito.sacar(saque);
							System.out.println("Saque de R$ " + saque + " realizado com sucesso!");
						} else {
							System.out.println("Operação inválida: Esta conta é de CRÉDITO e não aceita saque.");
						}
					} else {
						System.out.println("Conta não encontrada no sistema!");
					}
					break;

				case 5:
					System.out.println("====== Deposito ========");
					System.out.print("Insira o numero da conta para deposito: ");
					buscarNumeroConta = sc.nextDouble();
					sc.nextLine();

					contaEncontrada = buscarConta(contas, buscarNumeroConta);

					if (contaEncontrada != null) {
						System.out.print("Quanto deseja depositar: ");
						double deposito = sc.nextDouble();
						sc.nextLine();

						Conta contaGenerica = contaEncontrada.getConta();

						if (contaGenerica instanceof debito) {
							debito contaDebito = (debito) contaGenerica;
							contaDebito.depositar(deposito);
							System.out.println("Deposito de R$ " + deposito + " realizado com sucesso!");
						} else {
							System.out.println("Operação inválida: Esta conta é de CRÉDITO e não aceita deposito.");
						}
					} else {
						System.out.println("Conta não encontrada no sistema!");
					}
					break;

				case 6:
					System.out.println("====== Empréstimos ========");
					System.out.print("Insira o numero da conta para analise de Empréstimos: ");
					buscarNumeroConta = sc.nextDouble();
					sc.nextLine();

					contaEncontrada = buscarConta(contas, buscarNumeroConta);

					if (contaEncontrada != null) {
						Conta contaGenerica = contaEncontrada.getConta();

						if (contaGenerica instanceof debito) {
							debito contaDebito = (debito) contaGenerica;

							if (contaDebito.Analise() == EtapaEmprestimo.APROVADO) {
								System.out.print("Quanto deseja de Emprestimo: ");
								double emprestimo = sc.nextDouble();
								sc.nextLine();

								contaDebito.operacao(emprestimo);
								contaEncontrada.setStatusDebito(EtapaEmprestimo.APROVADO);
								System.out.println("\nEmprestimo de R$ " + emprestimo + " realizado com sucesso!");
							} else {
								contaEncontrada.setStatusDebito(EtapaEmprestimo.RECUSADO);
								System.out.println("Score muito baixo para empréstimo. Empréstimo RECUSADO");
							}
						} else {
							System.out.println("Operação inválida: Esta conta é de CRÉDITO e não aceita empréstimo.");
						}
					} else {
						System.out.println("Conta não encontrada no sistema!");
					}
					break;

				case 7:
					System.out.println("====== Limite de Crédito ========");
					System.out.print("Insira o numero da conta para analise de Limite: ");
					buscarNumeroConta = sc.nextDouble();
					sc.nextLine();

					contaEncontrada = buscarConta(contas, buscarNumeroConta);

					if (contaEncontrada != null) {
						Conta contaGenerica = contaEncontrada.getConta();

						if (contaGenerica instanceof credito) {
							credito contaCredito = (credito) contaGenerica;

							if (contaCredito.Analise() == EtapaLImite.AUMENTADO) {
								System.out.print("Quanto deseja de aumento de Limite: ");
								double limite = sc.nextDouble();
								sc.nextLine();

								contaCredito.operacao(limite);
								contaEncontrada.setStatusCredito(EtapaLImite.AUMENTADO);
								System.out.println("\nLimite aumentado de R$ " + limite + " realizado com sucesso!");
							} else {
								contaEncontrada.setStatusCredito(EtapaLImite.NEGADO);
								System.out.println("Score muito baixo para aumento. Limite NEGADO");
							}
						} else {
							System.out.println(
									"Operação inválida: Esta conta é de Débito e não aceita aumento de Limite.");
						}
					} else {
						System.out.println("Conta não encontrada no sistema!");
					}
					break;

				case 8:
					System.out.println("Programa Finalizado");
					break;
				default:
					System.out.println("Opção Inválida");
					break;
				}

			} catch (InputMismatchException e) {
				System.out.println("\nErro de Sistema: Você digitou letras onde eram esperados números!");
				sc.nextLine();
				menu = 0;
			} catch (BusinessException e) {
				System.out.println("\n Erro de Negócio: " + e.getMessage());
			} catch (Exception e) {
				System.out.println("\n Ocorreu um erro inesperado no sistema: " + e.getMessage());
			}
		}

		System.out.println("\nEncerrando os serviços do terminal bancário de forma segura");
		sc.close();
	}

	private static Banco buscarConta(List<Banco> contas, double numeroConta) {
		for (Banco c : contas) {
			if (c.getConta().getNumeroConta() == numeroConta) {
				return c;
			}
		}
		return null;
	}
}
