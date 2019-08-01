package co.rsk.net.statesync;

import co.rsk.net.NodeID;
import co.rsk.trie.Trie;
import org.ethereum.core.BlockHeader;
import org.ethereum.core.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public abstract class BaseStateSyncState implements StateSyncState {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final Factory factory;
    protected SyncConfiguration syncConfiguration;

    protected Duration timeElapsed;

    public BaseStateSyncState(Factory factory, SyncConfiguration syncConfiguration) {
        this.factory = factory;
        this.syncConfiguration = syncConfiguration;
        this.resetTimeElapsed();
    }

    protected void resetTimeElapsed() {
        timeElapsed = Duration.ZERO;
    }

    @Override
    public StateSyncState tick(Duration duration) {
        timeElapsed = timeElapsed.plus(duration);
        if (timeElapsed.compareTo(syncConfiguration.getTimeoutWaitingRequest()) >= 0) {
            logger.debug("Timeout on state {}, disabling sync state", this.getClass().getName());
            return factory.newDisabled();
        }
        return this;
    }

    @Override
    public StateSyncState newTrieNode(NodeID peerId, long requestId, Trie trieNode) {
        return this;
    }

    @Override
    public StateSyncState newBlockHeaders(List<BlockHeader> chunk) {
        return this;
    }

    @Override
    public StateSyncState newPeerStatus() {
        return this;
    }

    @Override
    public StateSyncState newBody(NodeID peerId,
                                  long requestId,
                                  List<Transaction> transactions,
                                  List<BlockHeader> uncles) {
        return this;
    }

    @Override
    public StateSyncState onEnter() {
        return this;
    }
}