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
		Map<Integer , Integer> decisionTreeRatings = new HashMap<Integer , Integer>();
		
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
		
	    List<String> atributos = new ArrayList<String>();
	    atributos.add("Action");
	    atributos.add("Adventure");
	    atributos.add("Animation");
	    atributos.add("Children's");
	    atributos.add("Comedy");
	    atributos.add("Crime");
	    atributos.add("Documentary");
	    atributos.add("Drama");
	    atributos.add("Fantasy");
	    atributos.add("Film-Noir");
	    atributos.add("Horror");
	    atributos.add("Musical");
	    atributos.add("Mystery");
	    atributos.add("Romance");
	    atributos.add("Sci-Fi");
	    atributos.add("Thriller");
	    atributos.add("War");
	    atributos.add("Western");
	    atributos.add("gender");
	    atributos.add("occupation");
	    atributos.add("age");
	    atributos.add("movieID");
	    
        for (int movieID : ids){
			int classAPriori = aPriori(movieID);
			int classArvoreDecisao = arvoreDecisao( ratings , new Atributos(atributos) , 
					    3 , me ,  getMovieById(movieID));
			if( classAPriori != 0){
				aPrioriRatings.put(movieID, classAPriori);
			}
			decisionTreeRatings.put(movieID, classArvoreDecisao);
		}
		
		int[][] matrizConfusaoAPriori = Comparador.matrizDeConfusao(aPrioriRatings , myRatings);
		int[][] matrizConfusaoDecisionTree = Comparador.matrizDeConfusao(decisionTreeRatings , myRatings);
		Formatador.formatar(matrizConfusaoAPriori , "a priori ");
		Formatador.formatar( matrizConfusaoDecisionTree, "arvore de decisão ");
		
	}
	
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
		
		List<Rating> exemplosi = new ArrayList<Rating>(); //na verdade vai ser so subconjunto de exemplos 
		//que tenha como valor do atributo que mais aumenta o ganho o valor da combinação
		//usuário-filme passado
		Atributos atributosi = atributos;
		//System.out.println(atributos.getAtributos().size());
		String melhor = maiorGanho( exemplos , atributos );
		atributosi.getAtributos().remove(melhor);// atributosi = atributos - melhor
		//atualizar exemplosi tirando o melhor
		System.out.println(melhor);
		if(melhor.equals("gender")){
			for (Rating exemplo : exemplos){
				if(getUserById(exemplo.getUserID()).getGender() == user.getGender()){
					exemplosi.add(exemplo);
				}
			}
		}
		else if ( melhor.equals("occupation")){
			for (Rating exemplo : exemplos){
				if(getUserById(exemplo.getUserID()).getOccupation() == user.getOccupation()){
					exemplosi.add(exemplo);
				}
			}
		}
		else if(melhor.equals("age")){
			for (Rating exemplo : exemplos){
				if(getUserById(exemplo.getUserID()).getAge() == user.getAge()){
					exemplosi.add(exemplo);
				}
			}
		}
		else if( melhor.equals("movieID")){
			for (Rating exemplo : exemplos){
				if( exemplo.getMovieID() == movie.getMovieID()){
					exemplosi.add(exemplo);
				}
			}
		}
		else {
			List<String> genres = new ArrayList<String>();
		    genres.add("Action");
		    genres.add("Adventure");
		    genres.add("Animation");
		    genres.add("Children's");
		    genres.add("Comedy");
		    genres.add("Crime");
		    genres.add("Documentary");
		    genres.add("Drama");
		    genres.add("Fantasy");
		    genres.add("Film-Noir");
		    genres.add("Horror");
		    genres.add("Musical");
		    genres.add("Mystery");
		    genres.add("Romance");
		    genres.add("Sci-Fi");
		    genres.add("Thriller");
		    genres.add("War");
		    genres.add("Western");
			for(String genre : genres){
				if(melhor.equals(genre)){
					for (Rating exemplo : exemplos){
						
						if(containsGenre(melhor , movie.getGenres()) && 
								containsGenre(melhor , getMovieById(exemplo.getMovieID()).getGenres())){
							exemplosi.add(exemplo);
						}
						else if(!containsGenre(melhor , movie.getGenres()) &&
								!containsGenre(melhor , getMovieById(exemplo.getMovieID()).getGenres())){
							exemplosi.add(exemplo);
						}
					}
				}
			}
		}
		int m = valorDaMaioria(exemplosi);
		//não existe necessidade de construir a árvore toda, apenas a subárvore relevante
		//ao par usuário-filme sendo avaliadoo
		return arvoreDecisao( exemplosi ,atributosi , m , user , movie);
	}
	
	public static boolean containsGenre(String genre , String[] genres){
		for( String mGenre : genres){
			if( mGenre.equals(genre) ) return true;
		}
		return false;
	}
	
	/*public static boolean hasGenre(String genre ,  String[] genres){
		for ( int i  = 0 ; i < genres.length ; i ++ ){
			if ( genres[i].equals(genre)) return true;
		}
		return false;
	}*/
	
	public static String maiorGanho( List<Rating> s , Atributos atributos){
	 	float maiorGanho = Integer.MIN_VALUE;
	 	String maiorGanhoAtr = null;
	 	for( String a : atributos.getAtributos() ){
	 	 	//System.out.println(atributos.getAtributos().size());
	 		if( ganho(s ,  a) > maiorGanho ){
	 	 		maiorGanho = ganho(s, a);
	 	 		maiorGanhoAtr = a;
	 	 	}
	 	}
		return maiorGanhoAtr;
	}
	
	public static float entropia(List<Rating> s){
		float[] p = {0,0,0,0,0};
		float sum_p = s.size();
		if(sum_p == 0) return 0;
		float entropia = 0;
		for ( Rating rating : s){
			p[rating.getRating() - 1] += 1;
		}
		for ( int i = 0 ; i < 5 ; i++){
			if ( p[i] != 0  ) 
				entropia += -(p[i]/sum_p)*Math.log10(p[i]/sum_p);
		}
		return entropia;
	}
	//TODO : precisa definir o que vai poder ser um atributo e quais os possíveis valores 
	//que ele pode assumir
	public static float ganho(List<Rating> s , String a){
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
		//System.out.println(modS);
		
		for ( int i = 0 ; i < genres.length ; i++){
			if(genres[i].equals(a)){
				List<Rating> sv = new ArrayList<Rating>();
				List<Rating> sv2 = new ArrayList<Rating>();
				for (Rating rating : s){ 
					//System.out.println(a);
					//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					//for (int j = 0 ; j < getMovieById(rating.getMovieID()).getGenres().length ; j++)
					//	System.out.println(getMovieById(rating.getMovieID()).getGenres()[j]);
					//System.out.println("-------------------------------------------");
					if(containsGenre(a, getMovieById(rating.getMovieID()).getGenres())){
						sv.add(rating);
					}
					else sv2.add(rating);
				}
				float modSv = sv.size() ;
				float modSv2 = sv2.size() ;
				//System.out.println(modSv);
				//System.out.println(modSv2);
				if(modSv != 0)
					sum += (modSv/modS)*entropia(sv);
				if(modSv2 != 0)
					sum +=  (modSv2/modS)*entropia(sv2);
			}
		}
		
		
		if (a.equals("age")){
			List<Rating> sv = new ArrayList<Rating>();
			for (int age : ages ){
				for (Rating rating : s){ 
					if(getUserById(rating.getUserID()).getAge() == age){
						sv.add(rating);
					}
				}
				float modSv = sv.size() ;
				if(modSv != 0)
					sum += (modSv/modS)*entropia(sv);
			}
					
		}
		else if(a.equals("gender")){
			List<Rating> sv = new ArrayList<Rating>();
			for (char gender : genders ){
				for (Rating rating : s){ 
					if(getUserById(rating.getUserID()).getGender() == gender){
						sv.add(rating);
					}
				}
				float modSv = sv.size() ;
				if(modSv != 0)
					sum += (modSv/modS)*entropia(sv);
			}
		}
		else if(a.equals("occupation")){
			List<Rating> sv = new ArrayList<Rating>();
			for (int occupation : occupations ){
				for (Rating rating : s){ 
					if(getUserById(rating.getUserID()).getOccupation() == occupation){
						sv.add(rating);
					}
				}
				float modSv = sv.size() ;
				if(modSv != 0)	
					sum += (modSv/modS)*entropia(sv);
			}
		}
		else if(a.equals("movieID")){
			List<Integer> movieIDs = new ArrayList<Integer>();
			for(Rating rating : s){
				int movieID = rating.getMovieID();
				if(!movieIDs.contains(movieID)){
					movieIDs.add(movieID);
				} 
			}
			List<Rating> sv = new ArrayList<Rating>();
			for (int movieID :  movieIDs){
				for (Rating rating : s){ 
					if(rating.getMovieID() == movieID){
						sv.add(rating);
					}
				}
				float modSv = sv.size() ;
				if(modSv != 0)	
					sum += (modSv/modS)*entropia(sv);
			}
		}
		//System.out.println(entropia(s));
		//System.out.println(sum);
		float ganho = entropia(s) - sum;
		return ganho;
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
		    //System.out.println(movieArguments[2].split("\\|")[0]);
		    mMovie.setGenres(movieArguments[2].split("\\|"));
		    //System.out.println(mMovie.getGenres()[0]);
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
