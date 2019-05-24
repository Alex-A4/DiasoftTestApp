package alexa4.friendphoto.ui;

import alexa4.friendphoto.repositories.DataRepository;

/**
 * Provider of DataRepository. Must be implemented from activity for fragments
 */
public interface RepositoryProvider {
    DataRepository getRepository();
}
