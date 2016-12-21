package dragonball.model.cell;

@SuppressWarnings("serial")
public class EmptyCell extends Cell {
	@Override
	public void onStep() {

	}

	@Override
	public String toString() {
		return "[ ]";
	}
}
