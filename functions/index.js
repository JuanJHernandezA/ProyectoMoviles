const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.deleteUserByAdmin = functions.https.onCall(async (data, context) => {
  // Validar que quien llama sea admin
  if (!context.auth || !context.auth.token.admin) {
    throw new functions.https.HttpsError("permission-denied", "No eres administrador");
  }

  const email = data.email;

  try {
    // Buscar UID por email
    const userRecord = await admin.auth().getUserByEmail(email);
    const uid = userRecord.uid;

    // Eliminar usuario
    await admin.auth().deleteUser(uid);

    return { message: `Usuario ${email} eliminado correctamente.` };
  } catch (error) {
    throw new functions.https.HttpsError("not-found", error.message);
  }
});
