package classficador;

public class Formatador {
	public static void formatar(int[][] matrizDeConfusao , String nomeClassificador){
		System.out.println("O classificador " + nomeClassificador + "retornou os "
				+ "seguintes resultados");
		System.out.println("Matriz de confusão");
		imprimirMatriz(matrizDeConfusao);
		//System.out.println("Taxa de acerto : " + Comparador.taxaDeAcerto(matrizDeConfusao));
		//System.out.println("Estatística Kappa : " + Comparador.estatisticaKappa(matrizDeConfusao));	
		//System.out.println("Erro Quadrático Médio  : " + Comparador.erroQuadraticoMedio(matrizDeConfusao));
		System.out.println("-----------------------------------------------------");
	
	}
	
	public static void imprimirMatriz(int[][] matriz){
		for ( int i = 0 ; i < 5 ; i ++){
			for (int j = 0 ; j < 5 ; j ++){
				System.out.print("|"+ matriz[i][j] +"|");
			}
			System.out.print("\n");
		}
	}
}
