package com.example.appAnime.activities.main.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.appAnime.R;
import com.example.appAnime.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    Boolean themeMain, themeSecond;
    FragmentSettingsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(getLayoutInflater());


       binding.titleCondiciones.setText("En el momento que usted, el usuario, nos facilita información de carácter personal a través de Animeflv.net (en adelante, el “Sitio Web”) se respeta su intimidad y los derechos que le reconoce la normativa sobre protección de datos de carácter personal. Por ello, es importante que entienda que información recabamos acerca de usted durante su visita y qué hacemos con dicha información la cual estará sujeta a la siguiente política sobre el tratamiento de datos personales.\n" +
                "Recomendamos leer detenidamente los siguientes puntos sobre nuestra Política de Privacidad; los que brindarán la total seguridad de que usted esta dentro de un sitio que protege su información e identidad. \n" +
        "Seguridad y protección de datos personales: La seguridad de sus datos personales es una prioridad para este sitio web el cual ofrece seguridad total. Sin embargo, no nos responsabilizamos por las actividades de hackers o terceros que realizan acciones para dañar romper la seguridad que cada sitio brinda. Teniendo en consideración las características técnicas de transmisión de información a través de Internet, ningún sistema es 100% seguro o exento de ataques.\n"+
        "Responsabilidad de opiniones: Este sitio web solo se responsabiliza de las publicaciones aquí expuestas a manera de posts, mas no de los comentarios de éstas, ya que son realizados por terceros y/o visitantes del sitio.\n" +
        "Su Privacidad: Este sitio web respeta la privacidad de cada uno de sus visitantes. Toda información ingresada por el usuario a través de nuestro sitio web, será tratada con la mayor seguridad, y sólo será usada de acuerdo con las limitaciones establecidas en este documento.\n"+
        "Obtención de información: Este sitio web obtiene los datos personales suministrados directa, voluntaria y conscientemente por cada usuario. La información personal que solicitamos corresponde a datos básicos los cuales serán solicitados a través de los diferentes formularios que se publiquen aquí.\n"+
                "Uso de la información: Al suministrar datos personales, automáticamente estará autorizándonos para usar sus datos personales de conformidad con nuestra Política de Privacidad, lo cual comprende los siguientes eventos: a) para el propósito específico para el cual la ha suministrado; b) para incrementar nuestra oferta al mercado y hacer publicidad de productos que pueden ser de sumo interés para el usuario; incluyendo los llamados para confirmación de su información; c) para personalizar y mejorar nuestros productos y servicios, y d) para enviar e-mails con nuestros boletines, responder inquietudes o comentarios, y mantener informado a nuestros usuarios.\n"+
                "Acceso a su información: El sitio web tiene el compromiso permanente de presentar nuevas soluciones que mejoren el valor de sus productos y servicios; con el objeto de ofrecer oportunidades especiales de mercado, como incentivos, promociones y novedades actualizadas. Animeflv no comercializa, vende ni alquila su base de datos a otras empresas.\n"+
              "Utilización de “Cookies”: Este sitio web utiliza “cookies” y dirección IP sólo para obtener información general de sus usuarios y para proveerles de un sitio personalizado. Para esto, mantenemos un registro de: browser, sistema operativo usado por el usuario/visitante, nombre del dominio del Proveedor de Servicio de Internet. Adicionalmente mantenemos un registro del número total de visitantes el que nos permite realizar mejoras en nuestro sitio. Los “cookies” permiten entregar un contenido ajustado a los intereses y necesidades de nuestros usuarios/visitantes. También podrían usarse cookies de Terceros que estén presentes en este Weblog, como anunciantes o publicidad del mismo, con el único fin de proveer informaciones adicionales o reelevantes a la Navegación del Usuario en este Sitio Web.\n"+
                "Revelación de información: En ningún momento se utiliza o revela a terceros, la información individual de los usuarios así como los datos sobre las visitas, o la información que nos proporcionan: nombre, dirección, dirección de correo electrónico, número telefónico, etc.\n"+
                "Modificaciones a nuestra Política de Privacidad: El sitio web se reserva en forma exclusiva el derecho de modificar, rectificar, alterar, agregar o eliminar cualquier punto del presente escrito en cualquier momento y sin previo aviso.\n"+
                "Estadísticas y otros Sitios Afines a Animeflv.\n"+
                "Este blog trabaja diferentes aplicaciones que evidentemente pueden hacer uso de Cookies almacenadas en su Equipo o utilizar algún script para el buen funcionamiento de este blog.\n"+
                "Actualmente estas cookies sólo retiran información estadística, en ningún caso buscan recopilar información de carácter importante.\n"
                );

       binding.txtPrivacidad.setText("La Política de Privacidad establece los términos en que AnimeFLV usa y protege la información que es proporcionada por sus usuarios al momento de utilizar su sitio web. Esta compañía está comprometida con la seguridad de los datos de sus usuarios. Cuando le pedimos llenar los campos de información personal con la cual usted pueda ser identificado, lo hacemos asegurando que sólo se empleará de acuerdo con los términos de este documento. Sin embargo esta Política de Privacidad puede cambiar con el tiempo o ser actualizada por lo que le recomendamos y enfatizamos revisar continuamente esta página para asegurarse que está de acuerdo con dichos cambios.\n" +
                "\n" +
                "Información que es recogida\n"+
                "Nuestro sitio web podrá recoger información personal por ejemplo: Nombre, información de contacto como su dirección de correo electrónica e información demográfica. Así mismo cuando sea necesario podrá ser requerida información específica para procesar algún pedido o realizar una entrega o facturación.\n" +
                "\n" +
                "Uso de la información recogida\n"+
                "Nuestro sitio web emplea la información con el fin de proporcionar el mejor servicio posible, particularmente para mantener un registro de usuarios, de pedidos en caso que aplique, y mejorar nuestros productos y servicios. Es posible que sean enviados correos electrónicos periódicamente a través de nuestro sitio con ofertas especiales, nuevos productos y otra información publicitaria que consideremos relevante para usted o que pueda brindarle algún beneficio, estos correos electrónicos serán enviados a la dirección que usted proporcione y podrán ser cancelados en cualquier momento. AnimeFLV está altamente comprometido para cumplir con el compromiso de mantener su información segura. Usamos los sistemas más avanzados y los actualizamos constantemente para asegurarnos que no exista ningún acceso no autorizado.\n" +
                "\n" +
                "Cookies\n"+
                "Una cookie se refiere a un fichero que es enviado con la finalidad de solicitar permiso para almacenarse en su ordenador, al aceptar dicho fichero se crea y la cookie sirve entonces para tener información respecto al tráfico web, y también facilita las futuras visitas a una web recurrente. Otra función que tienen las cookies es que con ellas las web pueden reconocerte individualmente y por tanto brindarte el mejor servicio personalizado de su web. Nuestro sitio web emplea las cookies para poder identificar las páginas que son visitadas y su frecuencia. Esta información es empleada únicamente para análisis estadístico y después la información se elimina de forma permanente. Usted puede eliminar las cookies en cualquier momento desde su ordenador. Sin embargo las cookies ayudan a proporcionar un mejor servicio de los sitios web, estás no dan acceso a información de su ordenador ni de usted, a menos de que usted así lo quiera y la proporcione directamente. Usted puede aceptar o negar el uso de cookies, sin embargo la mayoría de navegadores aceptan cookies automáticamente pues sirve para tener un mejor servicio web. También usted puede cambiar la configuración de su ordenador para declinar las cookies. Si se declinan es posible que no pueda utilizar algunos de nuestros servicios.\n" +
                "\n" +
                "Enlaces a Terceros\n" +
                "Este sitio web pudiera contener en laces a otros sitios que pudieran ser de su interés. Una vez que usted de clic en estos enlaces y abandone nuestra página, ya no tenemos control sobre al sitio al que es redirigido y por lo tanto no somos responsables de los términos o privacidad ni de la protección de sus datos en esos otros sitios terceros. Dichos sitios están sujetos a sus propias políticas de privacidad por lo cual es recomendable que los consulte para confirmar que usted está de acuerdo con estas.\n" +
                "\n" +
                "Control de su información personal\n" +
                "En cualquier momento usted puede restringir la recopilación o el uso de la información personal que es proporcionada a nuestro sitio web. Cada vez que se le solicite rellenar un formulario, como el de alta de usuario, puede marcar o desmarcar la opción de recibir información por correo electrónico. En caso de que haya marcado la opción de recibir nuestro boletín o publicidad usted puede cancelarla en cualquier momento. Esta compañía no venderá, cederá ni distribuirá la información personal que es recopilada sin su consentimiento, salvo que sea requerido por un juez con un orden judicial. AnimeFLV Se reserva el derecho de cambiar los términos de la presente Política de Privacidad en cualquier momento.\n" +
                "\n");

        binding.facebookLogo.setOnClickListener(view -> {
            String web = "www.facebook.com";
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, web);
            startActivity(intent);
        });

        binding.instagramLogo.setOnClickListener(view -> {
            String web = "www.instagram.com";
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, web);
            startActivity(intent);
        });

        binding.twitterLogo.setOnClickListener(view -> {
            String web = "www.twitter.com";
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, web);
            startActivity(intent);
        });

      binding.btnThemeMainOrange.setOnClickListener(view -> {
          getContext().setTheme(R.style.Theme_Basic_NoActionBar);
          System.out.println("Sirve el tema principal");
      });

        binding.btnThemeSecondMagenta.setOnClickListener(view -> {
            getContext().setTheme(R.style.SecondaryTheme_Basic_NoActionBar);

            System.out.println("Sirve el tema secundario");
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}