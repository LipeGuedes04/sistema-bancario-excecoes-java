package entities;

import model.exceptions.BusinessException;

public class debito extends Conta {

	double SaldoConta;

	public debito(double numeroConta, String nomeTitular, int score, double saldoConta) {
		super(numeroConta, nomeTitular, score);
		SaldoConta = saldoConta;
	}

	public double getSaldoConta() {
		return SaldoConta;
	}

	@Override
	public double operacao(double emprestimo) {
		if (getScore() >= 7) {
			if (emprestimo <= 0) {
				throw new BusinessException("Erro: Valor do empréstimo inválido");
			}
			double valorAtualizado = SaldoConta += emprestimo;
			return valorAtualizado;
		} else {
			return SaldoConta;
		}
	}	

	public EtapaEmprestimo Analise() {
		if (getScore() >= 7) {
			return EtapaEmprestimo.APROVADO;
		} else {
			return EtapaEmprestimo.RECUSADO;
		}
	}

	public void sacar(double quantia) {
		if (SaldoConta <= 0) {
			throw new BusinessException("Erro de Saque: A conta não possui saldo!");
		}
		if (quantia > SaldoConta) {
			throw new BusinessException("Erro de Saque: Saldo insuficiente!");
		}

		this.SaldoConta -= quantia;
	}

	public void depositar(double quantia) {
		if (quantia <= 0) {
			throw new BusinessException("Valor de deposito inválido");
		}
		this.SaldoConta += quantia;
	}

	@Override
	public String toString() {
	    return super.toString() + " | Saldo Atual: R$ " + String.format("%.2f", SaldoConta);
	}


}
