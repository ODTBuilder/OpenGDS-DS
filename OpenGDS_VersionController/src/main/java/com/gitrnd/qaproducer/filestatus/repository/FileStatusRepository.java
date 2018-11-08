package com.gitrnd.qaproducer.filestatus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gitrnd.qaproducer.filestatus.domain.FileStatus;
import com.gitrnd.qaproducer.filestatus.mapper.FileStatusMapper;

@Repository
public class FileStatusRepository {

	@Autowired
	private FileStatusMapper fileStatusMapper;

	public FileStatus retrieveFileStatusById(int fid) {
		return fileStatusMapper.retrieveFileStatusById(fid);
	}

	public void createFileStatus(FileStatus fileStatus) {
		fileStatusMapper.createFileStatus(fileStatus);
	}

	public void updateFileStatus(FileStatus fileStatus) {
		fileStatusMapper.updateFileStatus(fileStatus);
	}
	
	public boolean deleteFileStatus(FileStatus fs) throws RuntimeException {
		boolean flag = false;
		
		int response = fileStatusMapper.deleteFileStatus(fs);
		
		if(response>0){
			flag = true;
		}
		
		return flag;
	}
}
