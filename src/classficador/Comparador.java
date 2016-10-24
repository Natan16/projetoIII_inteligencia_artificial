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
		if ( elemMatrizConfusao != 0) return elemDiagonalPrincipal;
		return 0;
	}
	public int[][] matrizDeConfusao(){
		return new int[5][5];
	}
	
	public float erroQuadraticoMedio(){
		return 0;
	}
	
	public float estatisticaKappa(int[][] matrizDeConfusao){
		float pe = 1/5;
		float po = taxaDeAcerto(matrizDeConfusao); 
		return (po - pe)/(1-pe);
	}
}
