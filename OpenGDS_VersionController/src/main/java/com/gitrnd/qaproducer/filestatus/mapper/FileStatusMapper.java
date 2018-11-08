package com.gitrnd.qaproducer.filestatus.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gitrnd.qaproducer.filestatus.domain.FileStatus;

@Mapper
public interface FileStatusMapper {
	FileStatus retrieveFileStatusById(int fid);

	void createFileStatus(FileStatus fileStatus);

	void updateFileStatus(FileStatus fileStatus);
	
	int deleteFileStatus(FileStatus fs);
}
