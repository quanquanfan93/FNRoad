package com.example.administrator.fnroad.map;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/6/9 0009.
 */

public class TDTUrl {
    private static final String TAG = "TDTUrl";
    private TDTTiledServiceLayer.TDTTiledServiceType tdtTiledServiceType;
    private int level;
    private int col;
    private int row;

    private List<String> mStrings=new ArrayList<>();

    /**
     * 构造函数
     * @param level
     * @param col
     * @param row
     * @param tdtTiledServiceType
     */
    public TDTUrl(int level, int col, int row,TDTTiledServiceLayer.TDTTiledServiceType tdtTiledServiceType){
        this.level=level;
        this.col=col;
        this.row=row;
        this.tdtTiledServiceType=tdtTiledServiceType;
    }

    /**
     * 调用天地图url。
     * @return
     */
    public String generateUrl(){
        StringBuilder url=new StringBuilder("http://t");
        Random random=new Random();
        int subdomain = (random.nextInt(6) + 1);
        url.append(subdomain);
        switch(this.tdtTiledServiceType){
            case VEC_C:
                if(level<=18)
                url.append(".tianditu.com/DataServer?T=vec_c&X=").append(this.col).append("&Y=").append(this.row).append("&L=").append(this.level);
//              url.append(".tianditu.com/vec_c/wmts?request=GetTile&service=wmts&version=1.0.0&layer=vec&style=default&format=tiles&TileMatrixSet=c&TILECOL=").append(this.col).append("&TILEROW=").append(this.row).append("&TILEMATRIX=").append(this.level);
                else {
                    url=new StringBuilder("");
                    url.append("http://www.hytdt.gov.cn/ogcservice/HYEMAP/service/WMTS?Service=WMTS&VERSION=1.0.0&Request=GetTile&Layer=HYEMAP&Format=image/png&TileMatrixSet=TileMatrixSet0&TileMatrix=").
                            append(level).append("&Style=default&TileRow=").append(row).append("&TileCol=").append(col);
                }
//               url.append(".tianditu.com/vec_c/wmts?request=GetTile&service=wmts&version=1.0.0&layer=vec&style=default&format=tiles&TileMatrixSet=c&TILECOL=").append(this.col).append("&TILEROW=").append(this.row).append("&TILEMATRIX=").append(this.level);
                //url.append(".tianditu.com/DataServer?T=vec_w&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
                break;
            case CVA_C:
                if(level<=18)
                    url.append(".tianditu.com/DataServer?T=cva_c&X=").append(this.col).append("&Y=").append(this.row).append("&L=").append(this.level);
                else {
                    url=new StringBuilder("");
                    url.append("http://www.hytdt.gov.cn/ogcservice/HYEMAPANNO/service/WMTS?Service=WMTS&VERSION=1.0.0&Request=GetTile&Layer=HYEMAPANNO&Format=image/png&TileMatrixSet=TileMatrixSet0&TileMatrix=").
                            append(level).append("&Style=default&TileRow=").append(row).append("&TileCol=").append(col);
                }
//               url.append(".tianditu.com/DataServer?T=cva_c&X=").append(this.col).append("&Y=").append(this.row).append("&L=").append(this.level);
                //url.append(".tianditu.com/DataServer?T=cva_w&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
                break;
            case CIA_C:
                url.append(".tianditu.com/DataServer?T=cia_c&X=").append(this.col).append("&Y=").append(this.row).append("&L=").append(this.level);
                break;
            case IMG_C:
                url.append(".tianditu.com/DataServer?T=img_c&X=").append(this.col).append("&Y=").append(this.row).append("&L=").append(this.level);
                break;
            default:
                return null;
        }
        Log.e(TAG, "generateUrl: "+level );
        return url.toString();
    }
}
