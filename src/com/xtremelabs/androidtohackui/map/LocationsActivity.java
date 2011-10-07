package com.xtremelabs.androidtohackui.map;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.xtremelabs.androidtohackui.R;

public class LocationsActivity extends MapActivity {
    private static final int ZOOM_LEVEL = 16;
    private static final GeoPoint CONFERENCE_GEOPOINT = new GeoPoint(43641312, -79422742);
    private List<MapItem> mMapItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        setupMap();
        createMapItems();
        addMapItems();
    }

    private void setupMap() {
        mMapItems = new ArrayList<MapItem>(); 
        
        MapView map = getMapView();
        map.setBuiltInZoomControls(true);
        map.getController().setZoom(ZOOM_LEVEL);
        map.getController().setCenter(CONFERENCE_GEOPOINT);
        
    }

    private List<MapItem> createMapItems() {
        mMapItems.add(new MapItem(
                this,
                CONFERENCE_GEOPOINT,
                "99 SUDBURY",
                "Throughout the years, 99 SUDBURY has gone through many changes. Widely recognized as home to a restored glass factory, critically acclaimed restaurants, Studio 99 Film Studios, and even a famed after-hours club. The most recent ventures, a fitness club and an event space, have grown magnificently over the years and 99 SUDBURY has had the pleasure of welcoming such celebrity guests as Benicio Del Toro and Valentino."));
        
        mMapItems.add(new MapItem(
                this,
                new GeoPoint(43640718, -79436261),
                "Mother India Mother",
                "India Restaurant serves Indian cuisine, featuring recipes from both north and south of India. Mother India has been at the Queen Street West location for several years and is considered by many to be the best place for Indian roti in the neighborhood and perhaps the city - our guests can often be found traveling from out of the area to feed their craving for our rotis. We look forward to seeing you for lunch, dinner or take out soon!"));
        
        mMapItems.add(new MapItem(
                this,
                new GeoPoint(43641930, -79406542),
                "Niagara Street Cafe",
                "On the King Street West corridor, find your way to the inviting, unpretentious, Niagara Street Cafe. Owner-sommelier Anton Potvin, formery of Biff's and Canoe, has put his mark on this little gem. With reasonable prices and an ever changing organically based menu, you'll be hard pressed to find quality food for so little. Where will you discover a little piece of heaven like this? Go to the Niagara Street Cafe to contemplate fine dining and wine in a warm and romantic dining room with expert service."));
        
        mMapItems.add(new MapItem(
                this,
                new GeoPoint(43645641, -79411286),
                "Noce Restaurant",
                "Noce has been serving fine Italian cuisine for years and has a loyal returning clientele to show for it. Once you have been to Noce you are sure to return. Visit them for a business lunch, a romantic dinner or book a private party in one of their three rooms provided, and staff at Noce will ensure that your visit will be memorable."));

        mMapItems.add(new MapItem(
                this,
                new GeoPoint(43636914, -79437293),
                "Harry's Char Broil & Dining Lounge",
                "I may be biased, as this is the first greasy spoon I have been to in the area - but I like it. I'm actually not certain if it's on Springhurst - it's actually just by the No Frills , at the back end of the alleyway - but the address very well be correct, as the alleyway has no name to call its own."));

        return mMapItems;
    }

    private void addMapItems() {
        OnClickListener mapItemClickListener = new MapItemClickListener();

        for (MapItem mapItem : mMapItems) {
            LayoutParams params = createMapLayoutParams(mapItem.getGeoPoint());
            getMapView().addView(mapItem, params);
            mapItem.setOnClickListener(mapItemClickListener);
        }
    }
    
    private LayoutParams createMapLayoutParams(GeoPoint geoPoint) {
        MapView.LayoutParams params = new MapView.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                geoPoint, MapView.LayoutParams.BOTTOM_CENTER);
        params.mode = MapView.LayoutParams.MODE_MAP;
        
        return params;
    }
    
    private final class MapItemClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            MapItem mapItem = (MapItem) v;
            if (mapItem.isDescriptionVisible()) {
                mapItem.hideDescription();
            } else {
                hideAllDescriptions();
                mapItem.bringToFront();
                mapItem.showDescription();
            }
        }
    }
    
    public void hideAllDescriptions() {
        for (MapItem mapItem : mMapItems) {
            mapItem.hideDescription();
        }
    }
    
    private MapView getMapView() {
        MapView map = (MapView) findViewById(R.id.mapview);
        return map;
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
