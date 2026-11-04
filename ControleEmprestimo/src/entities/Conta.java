	package entities;
	
	public abstract class Conta {
	
		double NumeroConta;
		String NomeTitular;
		int Score;
	
		public Conta(double numeroConta, String nomeTitular, int score) {
			super();
			NumeroConta = numeroConta;
			NomeTitular = nomeTitular;
			Score = score;
		}
	
		public double getNumeroConta() {
			return NumeroConta;
		}
	
		public String getNomeTitular() {
			return NomeTitular;
		}
	
		public int getScore() {
			return Score;
		}
	
		public abstract double operacao(double valor);
	
		@Override
		public String toString() {
		    return "Titular: " + NomeTitular 
		        + " | Conta: " + NumeroConta 
		        + " | Score: " + Score;
		}
	
	}
