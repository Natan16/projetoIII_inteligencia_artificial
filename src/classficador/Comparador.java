package classficador;

import java.util.Map;

public class Comparador {
	public static float taxaDeAcerto(int[][] matrizDeConfusao){
		int elemDiagonalPrincipal = 0;
		int elemMatrizConfusao = 0;
		for ( int actual = 0 ; actual < 4 ; actual++){
			for ( int predicted = 0 ; predicted < 4 ; predicted++){
				if ( actual == predicted) {
					elemDiagonalPrincipal += matrizDeConfusao[actual][predicted];
				}
				elemMatrizConfusao += matrizDeConfusao[actual][predicted];
			}
		}
		if ( elemMatrizConfusao != 0) return elemDiagonalPrincipal/elemMatrizConfusao;
		return 0;
	}
	public static int[][] matrizDeConfusao(Map<Integer , Integer> predicted ,Map<Integer , Integer> actual){
		int[][] matrizConfusao = new int[5][5];
		for ( int i = 0 ; i < 5 ; i++){
			for ( int j = 0 ; j < 5 ; j++){
				matrizConfusao[i][j] = 0;
			}
		}
		for (int movieID : actual.keySet()){
			int classAPriori = predicted.get(movieID);
			if ( classAPriori != 0){
				matrizConfusao[actual.get(movieID) - 1][classAPriori - 1]++;
			}
		}
		return matrizConfusao;
	}
	//supondo que o estimador não tem viés
	public static float erroQuadraticoMedio(int[][] matrizDeConfusao){
		float EQM = 0;
		float totalEval = 0;
		for ( int actual = 0 ; actual < 4 ; actual++){
			for ( int predicted = 0 ; predicted < 4 ; predicted++){
			   EQM += ((float) matrizDeConfusao[actual][predicted])*Math.pow(actual - predicted , 2);
			   totalEval += (float) matrizDeConfusao[actual][predicted];
			}
		} 
		EQM = EQM/totalEval;
		return EQM;
	}
	
	public static float estatisticaKappa(int[][] matrizDeConfusao){
		float pe = 1/5;
		float po = taxaDeAcerto(matrizDeConfusao); 
		return (po - pe)/(1-pe);
	}
	
}
