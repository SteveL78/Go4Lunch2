package fr.steve.leroy.go4lunch.base;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Steve LEROY on 25/09/2021.
 */
public class BaseFragment extends Fragment implements EasyPermissions.PermissionCallbacks {


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
