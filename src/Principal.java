import java.lang.NumberFormatException;
public class Principal {
	// Método main
	public static void main(String[] args) {
		if ( args.length == 3 ) {
			try {
				// Convertimos los argumentos en números
				int size = Integer.parseInt(args[0]);
				int file = Integer.parseInt(args[2]);
				int rank = Integer.parseInt(args[1]);
				// Creamos un nuevo problema del viaje del caballo
				KnightTravelProblem problem = new KnightTravelProblem(size,file,rank);
				// Resolvemos el problema e imprimimos su solución (ojo, puede ser vacía)
				problem.knightTravel();
				System.out.println("The Knight Travel path is:");
				System.out.println(problem.printSolution());
			} catch ( NumberFormatException e ) {
				System.err.println("Error: parameters should be numeric.");
			} catch ( OutOfBoardException e ) {
				System.err.println("Error: initial square is out of board.");
			}
		} else {
			System.err.println("Error: you need to set three numeric parameters:");
			System.err.println("-Board size.");
			System.err.println("-File of initial Square.");
			System.err.println("-Rank of initial Square.");
		}
	}
}
