package cc.wanshan.gisdev.dao.plot;

import cc.wanshan.gisdev.entity.plot.PlotPolygon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PlotPolygonRepository extends JpaRepository<PlotPolygon, String>, JpaSpecificationExecutor<PlotPolygon> {
}
