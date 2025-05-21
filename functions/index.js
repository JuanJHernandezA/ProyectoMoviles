const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.deleteUserByAdmin = functions.https.onCall(async (data, context) => {
  console.log("Llamada a deleteUserByAdmin recibida");
  console.log("Datos recibidos:", data);
  const correo = data.correo;

  try {
    const userRecord = await admin.auth().getUserByEmail(correo);
    const uid = userRecord.uid;
    console.log(`UID encontrado para ${correo}: ${uid}`);

    await admin.auth().deleteUser(uid);
    console.log(`Usuario ${correo} eliminado de Auth`);

    return {message: `Usuario ${correo} eliminado correctamente.`};
  } catch (error) {
    console.error("Error al eliminar usuario:", error);
    throw new functions.https.HttpsError("not-found", error.message);
  }
});
