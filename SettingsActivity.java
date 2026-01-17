package com.example.smartnotes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

// Tambahkan "implements OnMapReadyCallback" untuk menangani peta
public class SettingsActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Komponen untuk Peta
    private MapView mapView;
    private GoogleMap googleMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    // Komponen untuk Lokasi
    private FusedLocationProviderClient fusedLocationClient;
    private TextView tvLocationStatus;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tvLocationStatus = findViewById(R.id.tv_location_status);
        Button btnGetLocation = findViewById(R.id.btn_get_location);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // --- Inisialisasi MapView ---
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this); // Ini akan memanggil onMapReady() saat peta siap

        btnGetLocation.setOnClickListener(v -> checkLocationPermission());

        setupBottomNavigation();
    }

    /**
     * Callback ini akan dipanggil setelah peta selesai dimuat dan siap digunakan.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        // Opsi tambahan: aktifkan tombol zoom
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        // Set lokasi awal sebelum lokasi asli didapat (contoh: Jakarta)
        LatLng defaultLocation = new LatLng(-6.2088, 106.8456);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f));
    }

    private void checkLocationPermission() {
        // Cek izin lokasi. Ini sudah benar.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Jika izin sudah ada, langsung ambil lokasi
            getLastLocation();
        }
    }

    private void getLastLocation() {
        try {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    String locText = "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude();
                    tvLocationStatus.setText("Lokasi Terdeteksi:\n" + locText);
                    Toast.makeText(this, "Lokasi Berhasil Diambil", Toast.LENGTH_SHORT).show();

                    // --- INI BAGIAN PENTING YANG DITAMBAHKAN ---
                    // Panggil method untuk update tampilan peta
                    updateMapLocation(location);

                } else {
                    tvLocationStatus.setText("Gagal mendapatkan lokasi. Pastikan GPS aktif.");
                    Toast.makeText(this, "Gagal mendapatkan lokasi. Pastikan GPS aktif.", Toast.LENGTH_SHORT).show();
                    Log.e("LocationError", "getLastLocation() returned null. GPS might be off.");
                }
            });
        } catch (SecurityException e) {
            // Ini terjadi jika izin tiba-tiba dicabut
            Toast.makeText(this, "Izin lokasi tidak diberikan.", Toast.LENGTH_SHORT).show();
            Log.e("SecurityError", "Location permission not granted.", e);
        }
    }

    /**
     * Method baru untuk memperbarui kamera peta dan menambahkan marker.
     * @param location Objek lokasi yang didapat dari FusedLocationProviderClient.
     */
    private void updateMapLocation(Location location) {
        if (googleMap == null) {
            Log.e("MapError", "GoogleMap object is null, cannot update location.");
            return; // Pastikan peta sudah siap sebelum digunakan
        }
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.clear(); // Hapus marker lama (jika ada)
        googleMap.addMarker(new MarkerOptions().position(currentLatLng).title("Lokasi Saya"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f)); // Pindahkan kamera dengan animasi
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Jika pengguna memberikan izin, ambil lokasi
                getLastLocation();
            } else {
                // Jika pengguna menolak izin
                Toast.makeText(this, "Izin lokasi ditolak.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.nav_settings);

        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, NoteListActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_add) {
                startActivity(new Intent(this, CreateNoteActivity.class));
                finish();
                return true;
            }
            return itemId == R.id.nav_settings;
        });
    }

    // --- WAJIB: Tambahkan semua method lifecycle untuk MapView ---

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
