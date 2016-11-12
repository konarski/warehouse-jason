package communication.model;

public enum PacketId {
	
	ENVTOA_At(0),
	ATOENV_Goto(1);
	
	private int value;
	 
	private PacketId(int value) {
		this.value = value;
	}
	
}
