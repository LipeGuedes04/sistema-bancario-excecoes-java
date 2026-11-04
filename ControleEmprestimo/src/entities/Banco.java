	package entities;
	
	public class Banco {
	
		private Conta conta;
		private EtapaEmprestimo statusDebito;
		private EtapaLImite statusCredito;
		
	
		public Banco(Conta conta, EtapaEmprestimo statusDebito) {
			this.conta = conta;
			this.statusDebito = EtapaEmprestimo.PENDENTE;
			this.statusCredito = null;
		}
	
		public Banco(Conta conta, EtapaLImite statusCredito) {
			this.conta = conta;
			this.statusCredito = EtapaLImite.PENDENTE;
			this.statusDebito = null; 
		}
		
		public Conta getConta() {
			return conta;
		}
	
		public EtapaEmprestimo getStatusDebito() {
			return statusDebito;
		}
	
		public void setStatusDebito(EtapaEmprestimo statusDebito) {
			this.statusDebito = statusDebito;
		}
	
		public EtapaLImite getStatusCredito() {
			return statusCredito;
		}
	
		public void setStatusCredito(EtapaLImite statusCredito) {
			this.statusCredito = statusCredito;
		}
	
		@Override
		public String toString() {
		    String statusAtual;
		    
		    if (statusCredito != null) {
		        statusAtual = "Crédito [" + statusCredito + "]";
		    } 
		    else {
		        statusAtual = "Empréstimo/Débito [" + statusDebito + "]";
		    }
		    	
		    return "[ CONTA ] " + conta + " || STATUS DO PROCESSO: " + statusAtual;
		}

	}
