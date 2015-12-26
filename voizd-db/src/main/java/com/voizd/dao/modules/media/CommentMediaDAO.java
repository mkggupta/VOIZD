package com.voizd.dao.modules.media;

import com.voizd.dao.entities.CommentMediaInfo;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;

public interface CommentMediaDAO {
	public void createCommentMedia(CommentMediaInfo commentMediaInfo) throws DataUpdateFailedException;
	public CommentMediaInfo getCommentMedia(Long commentId) throws DataAccessFailedException;
}
