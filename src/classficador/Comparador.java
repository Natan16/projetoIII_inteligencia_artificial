package classficador;

public class Comparador {
	public float taxaDeAcerto(int[][] matrizDeConfusao){
		int elemDiagonalPrincipal = 0;
		int elemMatrizConfusao = 0;
		for ( int actual = 0 ; actual < 4 ; actual++){
			for ( int predicted = 0 ; predicted < 4 ; predicted++){
				if ( actual == predicted) elemDiagonalPrincipal += 
						matrizDeConfusao[actual][predicted];
				elemMatrizConfusao += matrizDeConfusao[actual][predicted];
			}
		}
		if ( elemMatrizConfusao != 0) return elemDiagonalPrincipal/elemMatrizConfusao;
		return 0;
	}
	public float matrizDeConfusao(){
		return 0;
	}
	
	public float erroQuadraticoMedio(){
		return 0;
	}
	
	public float estatisticaKappa(){
		return 0;
	}
}
