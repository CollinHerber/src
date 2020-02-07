package scripts.MudRuneMaker.framework;

public class Vars {
	
	
		private static Vars vars;
		
		public static Vars get() {
			return vars == null? vars = new Vars() : vars;
		}
		
		public static void reset() {
			vars = new Vars();
		}
		
		// Script information
		public String status = "Initializing";
		public long runTime;
}
