package network.communicators;

import db.DataBaseManager;
import model.ServerObjectType;
import model.project.Tag;
import network.Communicable;
import network.DedicatedServer;
import network.DedicatedServerProvidable;

import java.awt.*;
import java.io.IOException;

public class TagEditedCommunicator implements Communicable {
    @Override
    public void communicate(DedicatedServer ds, DedicatedServerProvidable provider) {
        try {
            final String categoryId = ds.readData().toString();
            final String taskId = ds.readData().toString();
            final Tag tag = (Tag) ds.readData();
            final String tagName = ds.readData().toString();
            final Color tagColor = (Color) ds.readData();
            tag.setName(tagName);
            tag.setColor(tagColor);
            DataBaseManager.getInstance().getTagDBManager().editTag(taskId, tag);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.EDIT_TAG, categoryId);
            provider.sendBroadcast(ds.getHash(), null, taskId);
            provider.sendBroadcast(ds.getHash(), null, tag);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
