package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Tag;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.awt.*;
import java.io.IOException;
import java.util.UUID;

public class TagEditedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        try {
            final String taskId = ds.readData().toString();
            final Tag tag = (Tag) ds.readData();

            DataBaseManager.getInstance().getTagDBManager().addTag(tag, taskId);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.SET_TAG, tag);
            provider.sendBroadcast(ds.getHash(), null, taskId);
            provider.sendBroadcast(ds.getHash(), null, DataBaseManager.getInstance().getTagDBManager().getCategoryId(tag));

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
