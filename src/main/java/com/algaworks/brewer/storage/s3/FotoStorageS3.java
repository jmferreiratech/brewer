package com.algaworks.brewer.storage.s3;

import com.algaworks.brewer.storage.FotoStorage;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Profile("prod")
@Component
public class FotoStorageS3 implements FotoStorage {

    private static final Logger logger = LoggerFactory.getLogger(FotoStorageS3.class);
    private static final String BUCKET = "algaworks-brewer";

    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public String salvar(MultipartFile[] files) {
        String novoNome = null;
        if (files != null && files.length > 0) {
            MultipartFile arquivo = files[0];
            novoNome = renomearArquivo(arquivo.getOriginalFilename());

            AccessControlList acl = new AccessControlList();
            acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
            try {
                enviarFoto(novoNome, arquivo, acl);
                enviarThumbnail(THUMBNAIL_PREFIX + novoNome, arquivo, acl);
            } catch (IOException e) {
                throw new RuntimeException("Erro salvando arquivo no S3", e);
            }
        }
        return novoNome;
    }

    @Override
    public byte[] recuperar(String foto) {
        InputStream is = amazonS3.getObject(BUCKET, foto).getObjectContent();
        try {
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            logger.error("Não conseguiu recuperar foto do S3", e);
        }
        return new byte[0];
    }

    @Override
    public byte[] recuperarThumbnail(String foto) {
        return recuperar(THUMBNAIL_PREFIX + foto);
    }

    @Override
    public void excluir(String foto) {
        amazonS3.deleteObjects(new DeleteObjectsRequest(BUCKET)
                .withKeys(foto, THUMBNAIL_PREFIX + foto));
    }

    @Override
    public String getUrl(String nomeFoto) {
        if (!StringUtils.isEmpty(nomeFoto))
            return "https://s3-sa-east-1.amazonaws.com/algaworks-brewer/" + nomeFoto;
        return null;
    }

    private void enviarFoto(String nome, MultipartFile arquivo, AccessControlList acl) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(arquivo.getContentType());
        metadata.setContentLength(arquivo.getSize());
        amazonS3.putObject(new PutObjectRequest(BUCKET, nome, arquivo.getInputStream(), metadata)
                .withAccessControlList(acl));
    }

    private void enviarThumbnail(String nome, MultipartFile arquivo, AccessControlList acl) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(arquivo.getInputStream()).size(40, 68).toOutputStream(os);
        byte[] array = os.toByteArray();

        ObjectMetadata thumbMetadata = new ObjectMetadata();
        thumbMetadata.setContentType(arquivo.getContentType());
        thumbMetadata.setContentLength(array.length);
        amazonS3.putObject(new PutObjectRequest(BUCKET, nome, new ByteArrayInputStream(array), thumbMetadata)
                .withAccessControlList(acl));
    }
}
