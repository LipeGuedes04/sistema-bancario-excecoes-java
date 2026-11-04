package entities;

import model.exceptions.BusinessException;

public class credito extends Conta {

	double LimiteDisponivel;

	public credito(double numeroConta, String nomeTitular, int score, double limiteDisponivel) {
		super(numeroConta, nomeTitular, score);
		LimiteDisponivel = limiteDisponivel;

	}

	public double getLimiteDisponivel() {
		return LimiteDisponivel;
	}

	@Override
	public double operacao(double limite) {
		if (getScore() >= 7) {
			if (limite <= 0) {
				throw new BusinessException("Erro: Valor do limite inválido");
			}
				
			double LimiteAtualizado = LimiteDisponivel += limite;
			return LimiteAtualizado;

		} else {
			return LimiteDisponivel;
		}
	}

	public EtapaLImite Analise() {
		if (getScore() >= 7) {
			return EtapaLImite.AUMENTADO;
		} else {
			return EtapaLImite.NEGADO;
		}
	}

	@Override
	public String toString() {
	    return super.toString() + " | Limite Disponível: R$ " + String.format("%.2f", LimiteDisponivel);
	}


}
