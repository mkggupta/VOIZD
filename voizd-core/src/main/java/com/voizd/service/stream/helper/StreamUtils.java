package com.voizd.service.stream.helper;

import java.util.ArrayList;
import java.util.List;

import com.voizd.common.beans.vo.MediaVO;
import com.voizd.common.beans.vo.StreamMediaVO;
import com.voizd.common.beans.vo.StreamVO;
import com.voizd.common.constant.VoizdConstant;

public class StreamUtils {

	public static StreamVO getDefaultStreamVO(Long userId){
		StreamVO StreamVO = new StreamVO();
		StreamVO.setUserId(userId);
		StreamVO.setStreamName(VoizdConstant.DEFAULT_STREAM_NAME);
		StreamVO.setDescription(VoizdConstant.DEFAULT_STREAM_DESCRIPTION);
		StreamVO.setHasContent(true);
		StreamVO.setStatus(VoizdConstant.ACTIVE_STREAM_STATUS);
		StreamVO.setStreamState(VoizdConstant.PRIVATE_STREAM_STATUS);
		return StreamVO;
	} 
	
	public static StreamVO transformStream(StreamVO newStreamVO,StreamVO oldStreamVO){		
		StreamVO streamVO = null;
		if(newStreamVO != null){
			streamVO = new StreamVO();
			streamVO.setStreamId(oldStreamVO.getStreamId());
			if(newStreamVO.getTag()!=null){
				streamVO.setTag(newStreamVO.getTag());
			}else{
				streamVO.setTag(oldStreamVO.getTag());
			}
			if(newStreamVO.getDescription()!=null){
				streamVO.setDescription(newStreamVO.getDescription());
			}else{
			    streamVO.setDescription(oldStreamVO.getDescription());	
			}
			if(newStreamVO.getStreamState()!=null){
				streamVO.setStreamState(newStreamVO.getStreamState());
			}else{
			    streamVO.setStreamState(oldStreamVO.getStreamState());	
			}
		}
		return streamVO;
	}
	

	public static StreamMediaVO transformStationMedia(MediaVO mediaVO,Long streamId,int order){		
		StreamMediaVO streamMediaVO =null;
	    if(mediaVO != null){
	    	streamMediaVO = new StreamMediaVO();
	    	streamMediaVO.setFileId(mediaVO.getFileId());
	    	streamMediaVO.setMediaType(mediaVO.getMediaType());
	    	streamMediaVO.setExt(mediaVO.getMimeType());
	    	streamMediaVO.setStatus(VoizdConstant.MEDIA_ACTIVE_STATUS);
	    	streamMediaVO.setSize(mediaVO.getSize());
	    	streamMediaVO.setStreamId(streamId);
	    	streamMediaVO.setOrdering(order);
	    }
		return streamMediaVO;
	}
	
	public static List<String> getListOfFileId(String fileIds){		
		String files[] = fileIds.split("\\s*,\\s*");
		ArrayList<String> fileList= null;
		if(files != null && files.length >0){
			fileList = new ArrayList<String>();
		for(String fileId : files){
			fileList.add(fileId);
		  }
		}
		return fileList;
	}
	
}
