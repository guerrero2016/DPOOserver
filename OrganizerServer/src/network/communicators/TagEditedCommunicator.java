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
            if (tag.getId() == null || tag.getId().isEmpty()) {
                tag.setId(UUID.randomUUID().toString());
            }
            DataBaseManager.getInstance().getTagDBManager().addTag(tag, taskId);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.SET_TAG, tag);
            provider.sendBroadcast(ds.getHash(), null, taskId);
            provider.sendBroadcast(ds.getHash(), null, DataBaseManager.getInstance().getTagDBManager().getCategoryId(tag));

            /* Versió christian
            final String tagName = ds.readData().toString();
            final Color tagColor = (Color) ds.readData();
            tag.setName(tagName);
            tag.setColor(tagColor);
            DataBaseManager.getInstance().getTagDBManager().editTag(taskId, tag);
            provider.sendBroadcast(ds.getHash(), ServerObjectType.EDIT_TAG, categoryId);
            provider.sendBroadcast(ds.getHash(), null, taskId);
            provider.sendBroadcast(ds.getHash(), null, tag);*/

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
