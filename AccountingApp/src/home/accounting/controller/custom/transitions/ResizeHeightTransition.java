package home.accounting.controller.custom.transitions;

import javafx.animation.Transition;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class ResizeHeightTransition extends Transition{

	protected Region region;
    protected double startHeight;
    protected double newHeight;
    protected double heightDiff;
    
    public ResizeHeightTransition(Duration duration, Region region, double newWidth) {
		setCycleDuration(duration);
		this.region = region;
        this.newHeight = newWidth;
        this.startHeight = region.getWidth();
        this.heightDiff = newWidth - startHeight;
	}
    
	@Override
	protected void interpolate(double frac) {
		// TODO Auto-generated method stub
		region.setMinHeight( startHeight + ( heightDiff * frac ) );
	}

}
