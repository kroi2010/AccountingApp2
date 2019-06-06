package home.accounting.controller.custom.transitions;

import javafx.animation.Transition;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class ResizeWidthTransition extends Transition{

	protected Region region;
    protected double startWidth;
    protected double newWidth;
    protected double widthDiff;
    
    public ResizeWidthTransition(Duration duration, Region region, double newHeight) {
		setCycleDuration(duration);
		this.region = region;
        this.newWidth = newHeight;
        this.startWidth = region.getWidth();
        this.widthDiff = newWidth - startWidth;
	}
    
	@Override
	protected void interpolate(double frac) {
		// TODO Auto-generated method stub
		 region.setMinHeight( startWidth + ( widthDiff * frac ) );
	}

}
