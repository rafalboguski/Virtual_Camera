package boguskir.virtualcamera;

import java.util.Comparator;
import com.badlogic.gdx.math.Vector3;

public class MyRectangleComparator implements Comparator<MyRectangle> {

	private Vector3 cameraPos;

	public MyRectangleComparator(Vector3 cameraPos) {
		this.cameraPos = cameraPos;
	}

	@Override
	public int compare(MyRectangle o1, MyRectangle o2) {

		if (center(o1).dst(cameraPos) == center(o2).dst(cameraPos))
			return 0;
		if (center(o1).dst(cameraPos) < center(o2).dst(cameraPos))
			return 1;
		else
			return -1;

	}

	public static Vector3 center(MyRectangle A) {
		return A.getPoints().get(1).cpy().add(A.getPoints().get(2).cpy()).scl(0.5f);
	}

}
