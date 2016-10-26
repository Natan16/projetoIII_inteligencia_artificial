package classficador;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tabelas.*;



public class Classificador {
	public static List<Movie> movies;
	public static List<Rating> ratings;
	public static List<User> users;
	public static User me; //o usuário correspondente a mim 
	//minhas avaliações. A chave é a id do filme e o valor a o rating
	public static Map<Integer , Integer> myRatings = new HashMap<Integer , Integer>();
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args)  throws IOException {
		movies = new ArrayList<Movie>();
		ratings = new ArrayList<Rating>();
		users = new ArrayList<User>();
		User me = new User();
		Map<Integer , Integer> aPrioriRatings = new HashMap<Integer , Integer>();
		
		
		//preenchendo as informações sobre mim
		me.setAge(23);
		me.setGender('M');
		me.setOccupation(4);
		me.setZipcode("12228-461");
		//preenchendo as avaliações dos 10 filmes que eu já assiti
		int[] ids = {19,32,47,153,648,2571,2594,2628,2700,3409};
		myRatings.put(19, 4);
		myRatings.put(32, 2);
		myRatings.put(47, 5);
		myRatings.put(153,3);
		myRatings.put(648,3);
		myRatings.put(2571,5);
		myRatings.put(2594,5);
		myRatings.put(2628,2);
		myRatings.put(2700,4);
		myRatings.put(3409,1);
		
		leituraArmazenamento();
	           
        for (int movieID : ids){
			int classAPriori = aPriori(movieID);
			if ( classAPriori != 0){
				aPrioriRatings.put(movieID, classAPriori);
			}
		}
		
		int[][] matrizConfusaoAPriori = Comparador.matrizDeConfusao(aPrioriRatings , myRatings);
		Formatador.formatar(matrizConfusaoAPriori , "a priori ");
		Formatador.formatar(matrizConfusaoAPriori, "arvore de decisão ");
	 
	}
	
/*	public enum Animal {
		   GENEROS(0) , RAPOSA(1) , CARAMUJO(2) , CAVALO(3) , ZEBRA(4);
			public int value;

		    Animal(int valueArg){ 
		        value = valueArg; 
		    }
	}*/
	public static boolean mesmaClassificacao(List<Rating> exemplos){
		int rating = exemplos.get(0).getRating();
		for(Rating exemplo : exemplos){
			if (exemplo.getRating() != rating) return false;
		}
		return true;
	}
	
	public static int valorDaMaioria(List<Rating> exemplos){
		int valores[] = {0,0,0,0,0};
		int posMaioria = 0;
		for(Rating exemplo : exemplos){
			valores[exemplo.getRating() - 1]+=1;
		}
		for(int i  = 0 ; i  < 5 ; i++){
			if( valores[i] > posMaioria){
				posMaioria = i;
			}
		}
		return posMaioria + 1;
	}
	//no nosso caso, padrão = 3 ( pode ser melhorado tomando como base as avalizações do usuário )
	//código postal pode ser considerado irrelevante pra evitar overfitting 
	//ideia de melhoria : construir uma árvore de decisão pra cada combinação usuário-filme
	public static int arvoreDecisao(List<Rating> exemplos , Atributos atributos , 
	   int padrao , User user,  Movie movie){
		if (exemplos.size() == 0) return padrao;
		if (mesmaClassificacao(exemplos)) return exemplos.get(0).getRating();
		if ( atributos.ehVazio()) return valorDaMaioria(exemplos);
		
		List<Rating> exemplosi = exemplos; //na verdade vai ser so subconjunto de exemplos 
		//que tenha como valor do atributo que mais aumenta o ganho o valor da combinação
		//usuário-filme passado
		//ganho(exemplos , "genre");
		Atributos atributosi = atributos;
		int m = valorDaMaioria(exemplosi);
		
		return arvoreDecisao( exemplosi ,atributosi , m , user , movie);
	}
	
	public static float entropia(List<Rating> s){
		float[] p = {0,0,0,0,0};
		float sum_p = s.size();
		float entropia = 0;
		for ( Rating rating : s){
			p[rating.getRating() - 1] += 1;
		}
		for ( int i = 0 ; i < 5 ; i++){
			entropia += -(p[i]/sum_p)*Math.log10(p[i]/sum_p);
		}
		return entropia;
	}
	//TODO : precisa definir o que vai poder ser um atributo e quais os possíveis valores 
	//que ele pode assumir
	public static float ganho(List<Rating> s , String a , String tipo){
		String[] genres = {"Action",
							"Adventure",
							"Animation",
							"Children's",
							"Comedy",
							"Crime",
							"Documentary",
							"Drama",
							"Fantasy",
							"Film-Noir",
							"Horror",
							"Musical",
							"Mystery",
							"Romance",
							"Sci-Fi",
							"Thriller",
							"War",
							"Western"};
		int[] ages = {1, 18 , 25, 35, 45, 50 , 56};
		char[]  genders = {'M','F'};
		int[] occupations = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
		
		float sum = 0;
		float modS = s.size();
		if ( tipo.equals("genre")){
			for ( int i = 0 ; i < genres.length ; i++){
				if(genres[i].equals(a)){
					List<Rating> sv = new ArrayList<Rating>();
					List<Rating> sv2 = new ArrayList<Rating>();
					for (Rating rating : s){ 
						if(hasGenre(a, getMovieById(rating.getMovieID()).getGenres())){
							sv.add(rating);
						}
						else sv2.add(rating);
					}
					float modSv = sv.size() ;
					float modSv2 = sv2.size() ;
					sum += ((modSv/modS)*entropia(sv) + (modSv2/modS)*entropia(sv2));
				}
			}
		}
		
		else if (tipo.equals("age")){
			List<Rating> sv = new ArrayList<Rating>();
			for (int age : ages ){
				for (Rating rating : s){ 
					if(getUserById(rating.getUserID()).getAge() == age){
						sv.add(rating);
					}
				}
				float modSv = sv.size() ;
				sum += (modSv/modS)*entropia(sv);
			}
					
		}
		else if(tipo.equals("gender")){
			List<Rating> sv = new ArrayList<Rating>();
			for (char gender : genders ){
				for (Rating rating : s){ 
					if(getUserById(rating.getUserID()).getGender() == gender){
						sv.add(rating);
					}
				}
				float modSv = sv.size() ;
				sum += (modSv/modS)*entropia(sv);
			}
		}
		else if(tipo.equals("occupation")){
			List<Rating> sv = new ArrayList<Rating>();
			for (int occupation : occupations ){
				for (Rating rating : s){ 
					if(getUserById(rating.getUserID()).getOccupation() == occupation){
						sv.add(rating);
					}
				}
				float modSv = sv.size() ;
				sum += (modSv/modS)*entropia(sv);
			}
		}
		/*else if(tipo.equals("movieID")){
			
		}*/
		
		float ganho = entropia(s) - sum;
		return ganho;
	}
	
	public static boolean hasGenre(String genre ,  String[] genres){
		for ( int i  = 0 ; i < genres.length ; i ++ ){
			if ( genres[i].equals(genre)) return true;
		}
		return false;
	}
	
	public static int aPriori(int movieID){
		int soma = 0;
		int quantidade = 0;
		for(Rating rating : ratings ){
			if( rating.getMovieID() == movieID){
				soma += rating.getRating();
				quantidade++;
			}
		}
		if ( quantidade != 0) return soma/quantidade + (2*soma/quantidade)%2;
		return 0;
	}

	public static Movie getMovieById(int movieID){
		for (Movie movie : movies)
			if ( movie.getMovieID() == movieID) return movie;
		return null;
	}
	
	public static User getUserById(int userID){
		for (User user : users)
			if ( user.getUserID() == userID) return user;
		return null;
	}
	
	public static void leituraArmazenamento() throws NumberFormatException, IOException{
		DataInputStream inputMovies , inputRatings , inputUsers;
		//leitura e armazenamento de movies.dat
	    inputMovies = new DataInputStream(new FileInputStream("src/movies.dat"));
		while (inputMovies.available() > 0) {
			@SuppressWarnings("deprecation")
			String inputMoviesLine = inputMovies.readLine();
			String[] movieArguments = inputMoviesLine.split("::");
			Movie mMovie = new Movie();
			mMovie.setMovieID(Integer.parseInt(movieArguments[0]));
		    mMovie.setTitle(movieArguments[1]);
		    mMovie.setGenres(movieArguments[2].split("|"));
		    movies.add(mMovie);
		}            
        inputMovies.close();
        //leitura e armazenamento de ratings.dat
        inputRatings = new DataInputStream(new FileInputStream("src/ratings.dat"));
		while (inputRatings.available() > 0) {
			@SuppressWarnings("deprecation")
			String inputRatingsLine = inputRatings.readLine();
			String[] ratingArguments = inputRatingsLine.split("::");
			Rating mRating = new Rating();
			mRating.setUserID(Integer.parseInt(ratingArguments[0]));
			mRating.setMovieID(Integer.parseInt(ratingArguments[1]));
		    mRating.setRating(Integer.parseInt(ratingArguments[2]));
		    mRating.setTimestamp(Integer.parseInt(ratingArguments[3]));
			ratings.add(mRating);
		}            
        inputRatings.close();
        //leitura e armazenamento de users.dat
        inputUsers = new DataInputStream(new FileInputStream("src/users.dat"));
		while (inputUsers.available() > 0) {
			@SuppressWarnings("deprecation")
			String inputUsersLine = inputUsers.readLine();
			String[] userArguments = inputUsersLine.split("::");
			User mUser = new User();
			mUser.setUserID(Integer.parseInt(userArguments[0]));
			mUser.setGender(userArguments[1].charAt(0));
			mUser.setAge(Integer.parseInt(userArguments[2]));
			mUser.setOccupation(Integer.parseInt(userArguments[3]));
			mUser.setZipcode(userArguments[4]);
		    users.add(mUser);
		    //System.out.println(inputUsersLine);
		}            
        inputUsers.close();
	}
}
