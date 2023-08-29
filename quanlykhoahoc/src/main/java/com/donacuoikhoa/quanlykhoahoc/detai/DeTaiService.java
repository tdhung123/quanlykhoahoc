    package com.donacuoikhoa.quanlykhoahoc.detai;

    import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDung;
    import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDungNotFoundException;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.dao.DuplicateKeyException;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.Optional;

    @Service
    public class DeTaiService {
        @Autowired public   DeTaiRepository deTaiRepository;

        public List<DeTai> danhSach(){
            return deTaiRepository.findAll();

        }
        public void luu(DeTai deTai) {
            String tenDeTai = deTai.getTenDeTai();
            DeTai tenDeTaiTonTai = deTaiRepository.findByTenDeTai(tenDeTai);
            String maSo = deTai.getMaSo();
            DeTai maSoTonTai = deTaiRepository.findByMaSo(maSo);
            if (tenDeTaiTonTai != null && !tenDeTaiTonTai.getId().equals(deTai.getId())) {
                throw new DuplicateKeyException("Tên đề tài:" + tenDeTai + "' Đã tồn tại");
            }
            if (maSoTonTai != null && !maSoTonTai.getId().equals(deTai.getId())) {
                throw new DuplicateKeyException("mã số '" + maSo + "' đã tồn tại");
            }
            deTaiRepository.save(deTai);
        }

        public DeTai get(Integer id) throws DeTaiNotFoundException {
            Optional<DeTai> result = deTaiRepository.findById(id);
            if(result.isPresent()){
                return result.get();
            }
            throw new DeTaiNotFoundException("Không thể tìm thấy id người dùng: "+ id);
        }

        public void delete(Integer id) throws DeTaiNotFoundException {
            Integer cout = deTaiRepository.countById(id);
            if(cout == null || cout == 0){
                throw new DeTaiNotFoundException("Không thể tìm thấy id người dùng"+ id);
            }
            deTaiRepository.deleteById(id);
        }
    }
