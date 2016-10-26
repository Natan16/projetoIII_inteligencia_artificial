package classficador;

public class Formatador {
	public static void formatar(int[][] matrizDeConfusao , String nomeClassificador){
		System.out.println("O classificador " + nomeClassificador + "retornou os "
				+ "seguintes resultados");
		System.out.println("Matriz de confus�o");
		imprimirMatriz(matrizDeConfusao);
		//System.out.println("Taxa de acerto : " + Comparador.taxaDeAcerto(matrizDeConfusao));
		//System.out.println("Estat�stica Kappa : " + Comparador.estatisticaKappa(matrizDeConfusao));	
		//System.out.println("Erro Quadr�tico M�dio  : " + Comparador.erroQuadraticoMedio(matrizDeConfusao));
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
