package io.droidninja.feeder.ui.adapters;

import java.util.List;

import io.droidninja.feeder.api.model.SourceDTO;

/**
 * Created by Zeeshan on 2/12/17.
 */

public interface SelectedInterfaceListener {
    void onSelectedItems(List<SourceDTO> items);
}
