import com.home.croaton.followme.audio.AudioPlaybackController;
import com.home.croaton.followme.domain.AudioPoint;
import com.home.croaton.followme.domain.IExcursion;
import com.home.croaton.followme.domain.Route;
import com.home.croaton.followme.location.LocationHelper;
import com.home.croaton.followme.math.Vector2;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.osmdroid.util.GeoPoint;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class PointDirectionTest {

    @Test
    public void should_calculate_point_direction_along_line(){
        // Arrange
        Route route = new Route();
        route.addAudioPoint(new AudioPoint(0, new GeoPoint(1.0, 0.0), 1));
        route.addAudioPoint(new AudioPoint(1, new GeoPoint(3.0, 0.0), 1));
        route.addGeoPoint(0, new GeoPoint(0.0, 0.0));
        route.addGeoPoint(0, new GeoPoint(2.0, 0.0));
        route.addGeoPoint(0, new GeoPoint(4.0, 0.0));

        // Act
        route.generateDirections();

        // Assert
        Assert.assertEquals(new Vector2(1.0, 0.0), route.audioPoints().get(0).Direction);
        Assert.assertEquals(new Vector2(1.0, 0.0), route.audioPoints().get(1).Direction);
    }

    @Test
    public void should_calculate_point_direction(){
        // Arrange
        Route route = new Route();
        route.addAudioPoint(new AudioPoint(0, new GeoPoint(1.0, 3.0), 1));
        route.addAudioPoint(new AudioPoint(1, new GeoPoint(3.0, 3.0), 1));
        route.addGeoPoint(0, new GeoPoint(0.0, 0.0));
        route.addGeoPoint(0, new GeoPoint(2.0, 2.0));
        route.addGeoPoint(0, new GeoPoint(4.0, 4.0));

        // Act
        route.generateDirections();

        // Assert
        Assert.assertEquals(new Vector2(1.0, 1.0).normalize(), route.audioPoints().get(0).Direction);
        Assert.assertEquals(new Vector2(1.0, 1.0).normalize(), route.audioPoints().get(1).Direction);
    }

    @PrepareForTest(LocationHelper.class)
    @Test
    public void should_play_audioPoint_when_passing_point_in_correct_direction(){
        // Arrange
        Route route = new Route();
        route.addAudioPoint(new AudioPoint(0, new GeoPoint(3.0, 3.0), 1));
        route.addGeoPoint(0, new GeoPoint(2.0, 2.0));
        route.addGeoPoint(0, new GeoPoint(4.0, 4.0));
        route.generateDirections();
        GeoPoint currentLocation = new GeoPoint(3.1, 3.1);

        IExcursionBrief brief = Mockito.mock(IExcursionBrief.class);
        when(brief.getUseDirections()).thenReturn(true);

        IExcursion excursion = Mockito.mock(IExcursion.class);
        when(excursion.getRoute()).thenReturn(route);
        when(excursion.getBrief()).thenReturn(brief);

        AudioPlaybackController controller = new AudioPlaybackController("en", excursion);
        PowerMockito.mockStatic(LocationHelper.class);
        PowerMockito.when(LocationHelper.GetDistance(route.audioPoints().get(0).Position,
                currentLocation)).thenReturn(0.14f);

        // Act
        AudioPoint result = controller.getResourceToPlay(currentLocation, new Vector2(1.0, 1.0));

        // Assert
        Assert.assertNotNull(result);
    }

    @PrepareForTest(LocationHelper.class)
    @Test
    public void should_NOT_play_audioPoint_when_passing_point_in_wrong_direction(){
        // Arrange
        Route route = new Route();
        route.addAudioPoint(new AudioPoint(0, new GeoPoint(3.0, 3.0), 1));
        route.addGeoPoint(0, new GeoPoint(2.0, 2.0));
        route.addGeoPoint(0, new GeoPoint(4.0, 4.0));
        route.generateDirections();
        GeoPoint currentLocation = new GeoPoint(3.1, 3.1);

        IExcursionBrief brief = Mockito.mock(IExcursionBrief.class);
        when(brief.getUseDirections()).thenReturn(true);

        IExcursion excursion = Mockito.mock(IExcursion.class);
        when(excursion.getRoute()).thenReturn(route);
        when(excursion.getBrief()).thenReturn(brief);

        AudioPlaybackController controller = new AudioPlaybackController("en", excursion);
        PowerMockito.mockStatic(LocationHelper.class);
        PowerMockito.when(LocationHelper.GetDistance(route.audioPoints().get(0).Position,
                currentLocation)).thenReturn(0.14f);

        // Act
        AudioPoint result = controller.getResourceToPlay(currentLocation, new Vector2(-1.0, -1.0));

        // Assert
        Assert.assertNull(result);
    }
}